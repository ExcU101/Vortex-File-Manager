package io.github.excu101.vortex.ui.screen.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.provider.UnixFileSystemProvider
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.VortexFileManagerService
import io.github.excu101.vortex.base.utils.snackIt
import io.github.excu101.vortex.bindVortexService
import io.github.excu101.vortex.navigation.ActivityListener
import io.github.excu101.vortex.navigation.ActivityListenerRegister
import io.github.excu101.vortex.navigation.HostNavigationController
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.service.VortexService
import io.github.excu101.vortex.service.remote.VortexFileSystem
import io.github.excu101.vortex.startVortexService
import io.github.excu101.vortex.stopVortexService
import io.github.excu101.vortex.ui.component.BarOwner
import io.github.excu101.vortex.ui.component.FragmentAdapter
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.list.adapter.DrawerViewHolderFactories
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.navigation.AppNavigation
import io.github.excu101.vortex.ui.navigation.NavigationActions
import io.github.excu101.vortex.ui.navigation.ViewPagerNavigationController
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageFragment
import io.github.excu101.vortex.unbindVortexService

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity(),
    ServiceConnection, ThemeColorChangeListener, BarOwner,
    ActivityListenerRegister, HostNavigationController, ItemViewListener<Item<*>> {

    private val _controller by lazy {
        ViewPagerNavigationController(
            graph = AppNavigation.Graph,
            adapter = adapter!!,
            pager = binding?.pager!!
        )
    }

    override val controller: NavigationController
        get() = _controller

    private val provider = NavigationActivityProvider()
    private var binding: NavigationPageBinding? = null
    private var adapter: FragmentAdapter? = null

    var service: VortexFileManagerService? = null
        private set
    var isServiceBounded = false
        private set

    // Test data
    var isDarkTheme = true

    override val bar: Bar?
        get() = binding?.bar

    private val intentService by lazy {
        Intent(this, VortexService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startVortexService()
        adapter = FragmentAdapter(fragment = this)
        binding = NavigationPageBinding(context = this, adapter = adapter!!)
        binding?.onCreate()
        adapter?.addFragment(StorageListPageFragment())
        adapter?.addFragment(StorageListPageFragment())
        binding?.bar?.setNavigationClickListener { view ->
            ItemBottomDrawerFragment(
                this,
                *DrawerViewHolderFactories
            ).register(this).withItems(
                NavigationActions
            ).show()
        }
        binding?.pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                _controller.wrapCurrentSelection(adapter!![position])
            }
        })
        Theme.registerColorChangeListener(listener = this)

        isDarkTheme = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

        setContentView(binding?.root)
    }

    override fun onClick(view: View, item: Item<*>, position: Int) {
        when (item) {
            is DrawerItem -> {
                when (view.id) {
                    ViewIds.Navigation.Menu.SettingsId -> {
                        controller.navigate(route = AppNavigation.Routes.Settings.Page)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        bindVortexService(this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceBounded) {
            unbindVortexService(this)
        }
    }

    override fun onDestroy() {
        binding = null

        Theme.unregisterColorChangeListener(this)
        stopVortexService()
        super.onDestroy()
    }

    private fun notifyConnection(isConnected: Boolean = isServiceBounded) {
        bar?.let { view ->
            ThemeText(
                if (isConnected) {
                    vortexServiceConnectedKey
                } else {
                    vortexServiceDisconnectedKey
                }
            ).snackIt(view = view, anchorView = view)
        }
    }

    override fun onColorChanged() {
//        binding?.root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = VortexFileManagerService.Stub.asInterface(service)
        this.service = binder
        this.service?.installDefault(VortexFileSystem(local = UnixFileSystem(UnixFileSystemProvider())))
        isServiceBounded = true
        notifyConnection()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isServiceBounded = false
        notifyConnection()
    }

    override fun addListener(listener: ActivityListener) {
        provider.addListener(listener)
    }

    override fun removeListener(listener: ActivityListener) {
        provider.addListener(listener)
    }
}