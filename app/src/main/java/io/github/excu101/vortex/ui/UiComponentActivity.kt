package io.github.excu101.vortex.ui

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.vortex.R
import io.github.excu101.vortex.ui.component.menu.MenuLayout

@AndroidEntryPoint
class UiComponentActivity : AppCompatActivity() {

    private var root: FrameLayout? = null
    private val layout by lazy {
        MenuLayout(root!!.context).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = FrameLayout(this)

        setContentView(root)
        root!!.addView(layout)

        layout.addItem(action("Fuck", getDrawable(R.drawable.ic_folder_24)!!))
        layout.addItem(action("Fuck", getDrawable(R.drawable.ic_folder_24)!!))
        layout.addItem(action("Fuck", getDrawable(R.drawable.ic_folder_24)!!))
        layout.addItem(action("Fuck", getDrawable(R.drawable.ic_folder_24)!!))
    }

    override fun onDestroy() {
        super.onDestroy()
        root = null
    }

}