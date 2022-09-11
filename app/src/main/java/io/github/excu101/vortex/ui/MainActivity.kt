package io.github.excu101.vortex.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity.BOTTOM
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentContainerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.IVortexService
import io.github.excu101.vortex.R
import io.github.excu101.vortex.base.utils.snackIt
import io.github.excu101.vortex.service.VortexService
import io.github.excu101.vortex.ui.component.action.ActionDialog
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceConnectedKey
import io.github.excu101.vortex.ui.component.theme.key.vortexServiceDisconnectedKey
import io.github.excu101.vortex.ui.component.theme.value.initVortexDarkColorValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexLightColorValues
import io.github.excu101.vortex.ui.screen.list.StorageListFragment
import kotlin.math.sqrt

private val rootId = View.generateViewId()
private const val storageListTag = "STORAGE_LIST"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    ServiceConnection,
    ThemeSwitcherCallback,
    ThemeColorChangeListener {

    private var root: CoordinatorLayout? = null
    private var navigator: FragmentContainerView? = null

    private var service: IVortexService? = null
    private var isServiceBounded = false

    // Test data
    var isDarkTheme = true

    var bar: Bar? = null

    var drawer: ActionDialog? = null

    private val intentService by lazy {
        Intent(this, VortexService::class.java)
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        bar = Bar(context = this).apply {
            layoutParams = CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = BOTTOM
            }
        }
        drawer = ActionDialog(context = this)
        root?.addView(bar)
        ViewCompat.setOnApplyWindowInsetsListener(bar!!) { view, insets ->
            val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updatePadding(
                bottom = navigationInsets.bottom
            )
            insets
        }
        supportFragmentManager.beginTransaction()
            .replace(
                rootId,
                StorageListFragment(),
                storageListTag
            ).commit()
        setContentView(root)
    }

    override fun onStart() {
        super.onStart()
        root?.apply {
            bar?.navigationIcon = getDrawable(this@MainActivity, R.drawable.ic_menu_24)
            bar?.setNavigationClickListener { view ->
                drawer?.show()
            }
            Theme.notifyColorsChanged()
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(intentService)
        Theme.unregisterColorChangeListener(this)
        Theme.detachCallback(this)
        root = null
        bar = null
        drawer = null
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

        Theme.notifyColorsChanged()

        val animation = ViewAnimationUtils.createCircularReveal(root,
            width ?: 0,
            height ?: 0,
            startRadius,
            finalRadius
        ).apply {
            duration = 350L
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
        val binder = IVortexService.Stub.asInterface(service)
        this.service = binder
        isServiceBounded = true
        notifyConnection()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isServiceBounded = false
        notifyConnection()
    }
}