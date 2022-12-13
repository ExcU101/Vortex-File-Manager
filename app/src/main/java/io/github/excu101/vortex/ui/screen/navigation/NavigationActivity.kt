package io.github.excu101.vortex.ui.screen.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.pluginsystem.ui.theme.ThemeSwitcherCallback
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.*
import io.github.excu101.vortex.base.utils.snackIt
import io.github.excu101.vortex.navigation.ActivityListener
import io.github.excu101.vortex.navigation.ActivityListenerRegister
import io.github.excu101.vortex.navigation.HostNavigationController
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.service.VortexService
import io.github.excu101.vortex.ui.component.BarOwner
import io.github.excu101.vortex.ui.component.FragmentAdapter
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.callSelection
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.navigation.AppNavigation
import io.github.excu101.vortex.ui.navigation.ViewPagerNavigationController
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageFragment

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity(),
    ServiceConnection, ThemeSwitcherCallback, ThemeColorChangeListener, BarOwner,
    ActivityListenerRegister, HostNavigationController {

    override val controller: NavigationController by lazy {
        ViewPagerNavigationController(
            graph = AppNavigation.Graph,
            adapter = adapter!!,
            pager = binding?.pager!!
        )
    }

    private val provider = NavigationActivityProvider()
    private var binding: NavigationPageBinding? = null
    private var adapter: FragmentAdapter? = null

    var service: VortexFileManagerService? = null
        private set
    var isServiceBounded = false
        private set

    private val listener: ItemViewListener<DrawerItem> = ItemViewListener { view, item, position ->
        when (item.value.title) {
            "Switch theme" -> {
                Theme.switch()
            }
        }
    }

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
        binding?.pager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                adapter?.get(position).callSelection()
            }
        })
        Theme.attachCallback(callback = this)
        Theme.registerColorChangeListener(listener = this)

        isDarkTheme = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

        setContentView(binding?.root)
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
        Theme.detachCallback(this)
        stopVortexService()
        super.onDestroy()
    }

    override fun onSwitch() {
//        val width = binding?.root?.width?.div(2)
//        val height = binding?.root?.height?.div(2)
//        val startRadius = 0F
//        val finalRadius =
//            sqrt(((width?.times(width) ?: 0) + (height?.times(height) ?: 0)).toFloat())
//
//        isDarkTheme = if (!isDarkTheme) {
//            initOceanDarkColorValues()
//            true
//        } else {
//            initOceanLightColorValues()
//            false
//        }
//
//        val bitmap = createBitmap(binding?.root?.measuredWidth ?: 0,
//            binding?.root?.measuredHeight ?: 0,
//            Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        binding?.root?.draw(canvas)
//        binding?.screenImage?.setImageBitmap(bitmap)
//        binding?.screenImage?.visibility = VISIBLE
//
//        val animation = ViewAnimationUtils.createCircularReveal(binding?.root,
//            width?.div(2) ?: 0,
//            height?.div(2) ?: 0,
//            startRadius,
//            finalRadius).apply {
//            duration = 350L
//            addListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationStart(animation: Animator) {
//                    Theme.notifyColorsChanged()
//                }
//
//                override fun onAnimationEnd(animation: Animator) {
//                    binding?.screenImage?.setImageDrawable(null)
//                    binding?.screenImage?.visibility = GONE
//                }
//            })
//        }

//        animation.start()
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

    override fun onChanged() {
//        binding?.root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = VortexFileManagerService.Stub.asInterface(service)
        this.service = binder
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