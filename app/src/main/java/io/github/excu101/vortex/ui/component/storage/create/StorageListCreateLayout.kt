package io.github.excu101.vortex.ui.component.storage.create

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_FilledBox
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.fileListCreateDialogNameHintTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListCreateDialogPathHintTitleKey
import io.github.excu101.vortex.ui.component.theme.key.maxPathLengthKey

class StorageListCreateLayout(
    context: Context,
) : LinearLayout(context) {

    private val handle = ImageView(context).apply {
        background = MaterialShapeDrawable(
            builder()
                .setAllCorners(ROUNDED, 16F)
                .build()
        ).apply {
            setTint(Color.DKGRAY)
        }
    }

    private val inputNameLayout = TextInputLayout(
        context,
        null,
        Widget_MaterialComponents_TextInputLayout_FilledBox
    ).apply {
        hint = ThemeText(fileListCreateDialogNameHintTitleKey)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    private val inputName = TextInputEditText(context)

    var name: String
        set(value) = inputName.setText(value)
        get() = inputName.text.toString()

    private val inputPathLayout = TextInputLayout(
        context,
        null,
        Widget_MaterialComponents_TextInputLayout_FilledBox
    ).apply {
        hint = ThemeText(fileListCreateDialogPathHintTitleKey)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        helperText = "/ - separator"
        isCounterEnabled = true
        counterMaxLength = ThemeDimen(maxPathLengthKey)
    }

    private val inputPath = TextInputEditText(context)

    var path: String
        set(value) = inputPath.setText(value)
        get() = inputPath.text.toString()

    private val confirmButton = MaterialButton(context).apply {
        text = "Confirm"
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        background = MaterialShapeDrawable(
            builder()
                .setTopRightCorner(ROUNDED, 16F)
                .setTopLeftCorner(ROUNDED, 16F)
                .build()
        ).apply {
            setTint(Color.DKGRAY)
        }

        inputNameLayout.addView(inputName)
        inputPathLayout.addView(inputPath)

        updatePadding(
            top = 16.dp
        )

        addView(handle, LayoutParams(24.dp, 3.dp))
        addView(inputNameLayout)
        addView(inputPathLayout)
        addView(confirmButton)
    }

}