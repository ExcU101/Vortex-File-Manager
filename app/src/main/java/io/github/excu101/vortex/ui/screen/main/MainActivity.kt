package io.github.excu101.vortex.ui.screen.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.pluginsystem.utils.EmptyDrawable
import io.github.excu101.vortex.*
import io.github.excu101.vortex.base.utils.snackIt
import io.github.excu101.vortex.navigation.FragmentNavigationController
import io.github.excu101.vortex.navigation.HostNavigationController
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.service.VortexService
import io.github.excu101.vortex.ui.component.BarOwner
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.DrawerActionListener
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.text.navigation.navigationVortexFileManagerActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.component.theme.value.color.ocean.initOceanDarkColorValues
import io.github.excu101.vortex.ui.component.theme.value.color.ocean.initOceanLightColorValues
import io.github.excu101.vortex.ui.navigation.ActionNavigator
import io.github.excu101.vortex.ui.navigation.AppNavigation
import io.github.excu101.vortex.ui.navigation.NavigationActions
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    ServiceConnection, DrawerActionListener,
    ThemeSwitcherCallback, ThemeColorChangeListener, BarOwner, HostNavigationController {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            binding?.root?.hide()
        }
    }

    var binding: MainLayoutBinding? = null
        private set

    var service: VortexFileManagerService? = null
        private set
    var isServiceBounded = false
        private set

    override val controller: NavigationController by lazy {
        FragmentNavigationController
            .from(
                graph = AppNavigation.Graph,
                manager = supportFragmentManager,
                containerId = binding?.container?.id ?: -1
            )
            .bindOnBackPressedDispatcher(dispatcher = onBackPressedDispatcher)
            .enableOnBackPressedCallback(enabled = true)
    }

    private val navigator by lazy { ActionNavigator(controller) }

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
        binding = MainLayoutBinding(context = this)
        Theme.attachCallback(this)
        Theme.registerColorChangeListener(this)
        binding?.root?.registerListener(listener = this)
        binding?.root?.replaceNavigation(NavigationActions(ResourceProvider(this)).actions)
        binding?.bar?.setNavigationClickListener {
            binding?.root?.toggle()
        }
        ViewCompat.setOnApplyWindowInsetsListener(bar!!) { view, insets ->
            val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(bottom = navigationInsets.bottom)
            insets
        }

        isDarkTheme = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

        setContentView(binding?.root)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onStart() {
        super.onStart()
        bindVortexService(this, Context.BIND_AUTO_CREATE)
        binding?.apply {
            Theme.notifyColorsChanged()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isServiceBounded) {
            unbindVortexService(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVortexService()
        Theme.unregisterColorChangeListener(this)
        Theme.detachCallback(this)
        binding?.onDestroy()
    }

    override fun onDrawerActionCall(action: Action) {
        navigator.navigate(action) {
            when (action.title) {
                "Switch theme" -> {
                    Theme.switch()
                }
            }
        }
        binding?.root?.hide()
    }

    override fun onSwitch() {
        val width = binding?.root?.width?.div(2)
        val height = binding?.root?.height?.div(2)
        val startRadius = 0F
        val finalRadius =
            sqrt(((width?.times(width) ?: 0) + (height?.times(height) ?: 0)).toFloat())

        isDarkTheme = if (!isDarkTheme) {
            initOceanDarkColorValues()
            true
        } else {
            initOceanLightColorValues()
            false
        }

        val bitmap = createBitmap(binding?.root?.measuredWidth ?: 0,
            binding?.root?.measuredHeight ?: 0,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        binding?.root?.draw(canvas)
        binding?.screenImage?.setImageBitmap(bitmap)
        binding?.screenImage?.visibility = VISIBLE

        val animation = ViewAnimationUtils.createCircularReveal(binding?.root,
            width?.div(2) ?: 0,
            height?.div(2) ?: 0,
            startRadius,
            finalRadius).apply {
            duration = 350L
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    Theme.notifyColorsChanged()
                }

                override fun onAnimationEnd(animation: Animator) {
                    binding?.screenImage?.setImageDrawable(null)
                    binding?.screenImage?.visibility = GONE
                }
            })
        }

        animation.start()
    }

    private fun notifyConnection(isConnected: Boolean = isServiceBounded) {
        bar?.let { view ->
            ThemeText(if (isConnected) {
                vortexServiceConnectedKey
            } else {
                vortexServiceDisconnectedKey
            }).snackIt(view = view, anchorView = view)
        }
    }

    override fun onChanged() {
        binding?.root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
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
}