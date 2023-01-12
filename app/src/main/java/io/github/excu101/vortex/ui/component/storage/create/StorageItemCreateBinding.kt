package io.github.excu101.vortex.ui.component.storage.create

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import androidx.core.view.updatePadding
import com.google.android.material.R.layout.support_simple_spinner_dropdown_item
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.accentColorKey
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

class StorageItemCreateBinding(
    context: Context,
) : ViewBinding<LinearLayout> {

    override val root = LinearLayout(context).apply {
        background = this@StorageItemCreateBinding.background
    }

    private val background = MaterialShapeDrawable().apply {
        setTint(ThemeColor(mainDrawerBackgroundColorKey))
        elevation = 0F
    }

    val nameLayout = TextInputLayout(
        context
    ).apply {
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

    val name = TextInputEditText(nameLayout.context).apply {
        hint = "Enter name"
    }

    val pathLayout = TextInputLayout(
        context,
    ).apply {
        setBoxCornerRadii(
            16F.dp,
            16F.dp,
            16F.dp,
            16F.dp
        )
        setBoxCornerFamily(CornerFamily.ROUNDED)
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
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

    val path = TextInputEditText(pathLayout.context).apply {
        hint = "Enter path"
    }

    val typeLayout = TextInputLayout(
        context,
    ).apply {
        setBoxCornerRadii(
            16F.dp,
            16F.dp,
            16F.dp,
            16F.dp
        )
        setBoxCornerFamily(CornerFamily.ROUNDED)
        endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
        boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_FILLED
        hintTextColor = ColorStateList.valueOf(ThemeColor(accentColorKey))
        endIconDrawable?.setTint(ThemeColor(accentColorKey))
        boxStrokeColor = ThemeColor(accentColorKey)
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
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

    val types = arrayOf("File", "Directory")

    val adapter = ArrayAdapter(
        context,
        support_simple_spinner_dropdown_item,
        types
    )

    val type = MaterialAutoCompleteTextView(typeLayout.context).apply {
        inputType = InputType.TYPE_NULL
        hint = "Enter type"
        background = null
        overlay

        setAdapter(this@StorageItemCreateBinding.adapter)
        layoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT,
            WRAP_CONTENT
        )
        setDropDownBackgroundDrawable(null)
        updatePadding(
            top = 24.dp,
            bottom = 10.dp,
            left = 16.dp,
            right = 16.dp
        )
    }

    val modeLayout = TextInputLayout(
        context,
    ).apply {
        setBoxCornerFamily(CornerFamily.ROUNDED)
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
        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        placeholderText = "777"
    }

    val mode = TextInputEditText(pathLayout.context).apply {
        hint = "Enter mode"
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        setText("777")
    }

    init {
        onCreate()
    }

    override fun onCreate() {
        root.orientation = VERTICAL
        nameLayout.addView(name)
        pathLayout.addView(path)
        typeLayout.addView(type)
        modeLayout.addView(mode)
        root.addView(nameLayout)
        root.addView(pathLayout)
        root.addView(typeLayout)
        root.addView(modeLayout)
    }

    override fun onDestroy() {

    }

}