package io.github.excu101.vortex.ui.component.rename

import android.content.Context
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_FilledBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class StorageListRenameLayout(
    context: Context,
) : LinearLayout(context) {

    private val layout = TextInputLayout(
        ContextThemeWrapper(
            context, Widget_MaterialComponents_TextInputLayout_FilledBox
        )
    ).apply {

    }

    private val input = TextInputEditText(context).apply {

    }

    init {
        orientation = VERTICAL
        layout.addView(input)
        addView(layout)
    }

}