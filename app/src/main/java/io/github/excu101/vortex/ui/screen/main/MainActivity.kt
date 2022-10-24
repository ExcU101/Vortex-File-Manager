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
import android.view.Gravity.BOTTOM
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.R
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.base.utils.snackIt
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.BottomActionDrawer
import io.github.excu101.vortex.ui.component.drawer.DrawerActionListener
import io.github.excu101.vortex.ui.component.fragment.FragmentContainerView
import io.github.excu101.vortex.ui.component.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.component.theme.value.initVortexDarkColorValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexLightColorValues
import io.github.excu101.vortex.ui.screen.main.MainViewModel.Companion.rootId
import io.github.excu101.vortex.utils.vortexPackageName
import io.github.excu101.vortex.utils.vortexServiceActionName
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    ServiceConnection,
    DrawerActionListener,
    ThemeSwitcherCallback,
    ThemeColorChangeListener {

    private val viewModel by viewModels<MainViewModel>()

    private var root: CoordinatorLayout? = null
    private var themeImageView: ImageView? = null

    var service: VortexServiceApi? = null
        private set
    var isServiceBounded = false
        private set

    // Test data
    var isDarkTheme = true

    var bar: Bar? = null
        private set

    private var container: FragmentContainerView? = null

    var drawer: BottomActionDrawer? = null
        private set

    private val intentService by lazy {
        Intent(vortexServiceActionName).apply {
            `package` = vortexPackageName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(intentService)
        bindService(
            intentService,
            this,
            Context.BIND_AUTO_CREATE
        )
        Theme.attachCallback(this)
        Theme.registerColorChangeListener(this)

        root = CoordinatorLayout(this).apply {
            id = rootId
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        bar = Bar(root!!.context).apply {

        }
        container = FragmentContainerView(root!!.context, manager = supportFragmentManager)
        themeImageView = ImageView(this).apply {
            visibility = GONE
        }
        drawer = BottomActionDrawer(context = this)
        drawer?.registerListener(listener = this)
        root?.addView(container, MATCH_PARENT, MATCH_PARENT)
        root?.addView(bar, CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = BOTTOM
        })
        root?.addView(themeImageView, MATCH_PARENT, MATCH_PARENT)
        ViewCompat.setOnApplyWindowInsetsListener(bar!!) { view, insets ->
            val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(
                bottom = navigationInsets.bottom
            )
            insets
        }

        isDarkTheme = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

        setContentView(root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectState { state ->
                    container?.setFragment(state.fragment, state.tag)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        root?.apply {
            bar?.navigationIcon = getDrawable(this@MainActivity, R.drawable.ic_menu_24)
            Theme.notifyColorsChanged()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(intentService)
        service = null
        Theme.unregisterColorChangeListener(this)
        Theme.detachCallback(this)
        root = null
        bar = null
        drawer = null
    }

    override fun onDrawerActionCall(action: Action) {
        when (action.title) {
            "Switch theme" -> {
                Theme.switch()
            }
        }
        drawer?.hide()
    }

    override fun onSwitch() {
        val width = root?.width?.div(2)
        val height = root?.height?.div(2)
        val startRadius = 0F
        val finalRadius =
            sqrt(((width?.times(width) ?: 0) + (height?.times(height) ?: 0)).toFloat())

        isDarkTheme = if (!isDarkTheme) {
            initVortexDarkColorValues()
            true
        } else {
            initVortexLightColorValues()
            false
        }

        val bitmap = createBitmap(
            root?.measuredWidth ?: 0,
            root?.measuredHeight ?: 0,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        root?.draw(canvas)
        themeImageView?.setImageBitmap(bitmap)
        themeImageView?.visibility = VISIBLE


        val animation = ViewAnimationUtils.createCircularReveal(root,
            (bitmap.width / 2) ?: 0,
            (bitmap.height / 2) ?: 0,
            startRadius,
            finalRadius
        ).apply {
            duration = 350L
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    Theme.notifyColorsChanged()
                }

                override fun onAnimationEnd(animation: Animator) {
                    themeImageView?.setImageDrawable(null)
                    themeImageView?.visibility = GONE
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
            }).snackIt(
                view = view,
                anchorView = view
            )
        }
    }

    override fun onChanged() {
        root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//        val binder = IVortexService.Stub.asInterface(service)
//        this.service = binder
        isServiceBounded = true
        notifyConnection()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isServiceBounded = false
        notifyConnection()
    }
}