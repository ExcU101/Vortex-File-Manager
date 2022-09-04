package io.github.excu101.vortex.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity.BOTTOM
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
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ui.component.action.ActionDialog
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.screen.list.StorageListFragment

private const val rootId = 1
private const val storageListTag = "STORAGE_LIST"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var root: CoordinatorLayout? = null
    private var navigator: FragmentContainerView? = null

    var bar: Bar? = null

    var drawer: ActionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            background = ColorDrawable(ThemeColor(backgroundColorKey))
            bar?.navigationIcon = getDrawable(this@MainActivity, R.drawable.ic_menu_24)
            bar?.setNavigationClickListener { view ->
                drawer?.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        root = null
        bar = null
        drawer = null
    }
}