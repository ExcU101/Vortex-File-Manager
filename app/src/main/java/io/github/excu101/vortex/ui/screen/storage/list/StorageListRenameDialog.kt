package io.github.excu101.vortex.ui.screen.storage.list

import android.content.Context
import android.os.Bundle
import io.github.excu101.vortex.ui.component.sheet.BottomSheetDialog
import io.github.excu101.vortex.ui.component.storage.rename.StorageListRenameLayout

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