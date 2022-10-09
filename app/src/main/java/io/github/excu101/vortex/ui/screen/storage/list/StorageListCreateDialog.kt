package io.github.excu101.vortex.ui.screen.storage.list

import android.content.Context
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class StorageCreateDialog(context: Context) : BottomSheetDialog(context) {

    private val root = CoordinatorLayout(context)

    private val input = TextInputEditText(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}