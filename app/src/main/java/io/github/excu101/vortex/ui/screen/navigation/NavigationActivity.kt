package io.github.excu101.vortex.ui.screen.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.getInsetsController
import io.github.excu101.vortex.theme.model.Color
import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.ThemeColorChangeListener
import io.github.excu101.vortex.navigation.NavRestorer
import io.github.excu101.vortex.navigation.current
import io.github.excu101.vortex.navigation.onActivityResult
import io.github.excu101.vortex.navigation.onBackActivated
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.navigation.Navigation
import io.github.excu101.vortex.ui.navigation.Navigation.Host
import io.github.excu101.vortex.ui.navigation.Navigation.Storage
import io.github.excu101.vortex.ui.navigation.VortexNavigationController


class NavigationActivity : ComponentActivity(),
    io.github.excu101.vortex.theme.ThemeColorChangeListener, MenuActionListener,
    NavRestorer {

    private var navigation: VortexNavigationController? = null

    private var root: FrameLayout? = null

    override fun onCreate(inState: Bundle?) {
        super.onCreate(inState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainBarSurfaceColorKey) > 0.5) {
            with(getInsetsController(window, window.decorView)) {
                isAppearanceLightNavigationBars = false
                isAppearanceLightStatusBars = false
            }
        } else {
            with(getInsetsController(window, window.decorView)) {
                isAppearanceLightNavigationBars = true
                isAppearanceLightStatusBars = true
            }
        }

        window.statusBarColor = io.github.excu101.vortex.theme.model.Color.Transparent.value
        window.navigationBarColor = io.github.excu101.vortex.theme.model.Color.Transparent.value

        navigation = VortexNavigationController(context = this, restoration = this)
        navigation?.onCreate()
        root = FrameLayout(this).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        navigation?.root?.let { root?.addView(it) }
        navigation?.bar?.let { root?.addView(it) }
        navigation?.bar?.registerListener(this)

        setContentView(root)

        if (inState != null) {
            navigation?.restoreNavigation(inState)
        } else {
            navigation?.init(Storage(context = this))
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigation?.saveNavigation(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (navigation?.onBackActivated() == false) super.onBackPressed()
    }

    override fun onMenuActionCall(action: MenuAction): Boolean {
        if (navigation?.current is MenuActionListener) {
            if ((navigation?.current as? MenuActionListener)?.onMenuActionCall(action) == true) return true
        }
        return false
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (navigation?.onActivityResult(requestCode, resultCode, data) == true) return
    }

    override fun onRestoreController(context: Context, route: Int): PageController? {
        return when (route) {
            Navigation.Settings -> null
            Host -> Host(context as NavigationActivity)
            Storage -> Storage(context as NavigationActivity)
            else -> null
        }
    }

    override fun onDestroy() {
        io.github.excu101.vortex.theme.Theme.unregisterColorChangeListener(this)
        super.onDestroy()
    }

    override fun onColorChanged() {
//        binding?.root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
    }
}