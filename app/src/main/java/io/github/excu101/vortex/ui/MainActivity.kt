package io.github.excu101.vortex.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.R
import io.github.excu101.vortex.databinding.ActivityMainBinding
import io.github.excu101.vortex.ui.theme.key.backgroundColorKey
import io.github.excu101.vortex.ui.view.action.ActionListDialog
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    val list by lazy(NONE) {
        ActionListDialog(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onStart() {
        super.onStart()
        binding?.apply {
            root.background = ColorDrawable(ThemeColor(backgroundColorKey))
            bar.setNavigationIcon(R.drawable.ic_menu_24)
            bar.setNavigationOnClickListener {
                list.show()
            }
            ViewCompat.setOnApplyWindowInsetsListener(bar) { view, insets ->
                val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                view.updatePadding(
                    bottom = navigationInsets.bottom
                )
                insets
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}