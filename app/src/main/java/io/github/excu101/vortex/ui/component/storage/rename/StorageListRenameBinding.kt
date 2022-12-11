package io.github.excu101.vortex.ui.component.storage.rename

import android.content.Context
import android.content.res.ColorStateList
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.accentColorKey

class StorageListRenameBinding(
    context: Context,
) : ViewBinding<LinearLayout> {

    override val root = LinearLayout(context).apply {
        orientation = VERTICAL
    }

    val layout = TextInputLayout(
        context
    ).apply {
        hint = "Input new name"
        setBoxCornerFamily(CornerFamily.ROUNDED)
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        setBoxCornerRadii(
            16F.dp,
            16F.dp,
            16F.dp,
            16F.dp
        )
        boxStrokeColor = ThemeColor(accentColorKey)
        hintTextColor = ColorStateList.valueOf(ThemeColor(accentColorKey))
        boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_FILLED
        layoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).apply {
            rightMargin = 16
            leftMargin = 16
            topMargin = 16
            bottomMargin = 16
        }
    }

    val input = TextInputEditText(layout.context).apply {

    }

    val confirm = MaterialButton(context).apply {
        text = "Confirm"
        layoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        ).apply {
            leftMargin = 16
            rightMargin = 16
        }
        setBackgroundColor(ThemeColor(accentColorKey))
        cornerRadius = 16.dp
    }

    init {
        onCreate()
    }

    override fun onCreate() {
        layout.addView(input)
        root.addView(layout)
        root.addView(confirm)
    }

    override fun onDestroy() {
        root.removeAllViews()
    }

}