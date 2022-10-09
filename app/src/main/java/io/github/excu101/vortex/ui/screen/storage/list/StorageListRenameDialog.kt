package io.github.excu101.vortex.ui.screen.storage.list

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_FilledBox
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.excu101.vortex.ui.component.rename.StorageListRenameLayout

class StorageListRenameDialog(
    context: Context,
) : BottomSheetDialog(
    context
) {

    private val root = StorageListRenameLayout(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(root)
    }


}