package io.github.excu101.vortex.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity.BOTTOM
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.databinding.ActivityMainBinding
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.view.action.ActionListDialog
import io.github.excu101.vortex.ui.view.bar.BottomBar
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    val bar by lazy(NONE) {
        BottomBar(this).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = BOTTOM
            }
        }
    }

    val list by lazy(NONE) {
        ActionListDialog(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

    }

    override fun onStart() {
        super.onStart()
        binding?.apply {
            root.addView(bar)
            root.background = ColorDrawable(Theme<Int, Color>(backgroundColorKey))
            ViewCompat.setOnApplyWindowInsetsListener(bar) { view, insets ->
                view.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
                insets
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}