package io.github.excu101.vortex.ui.screen.storage

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.vortex.ui.component.storage.rename.StorageListRenameBinding

typealias ItemRenameDialogResult = (dest: Path) -> Unit

class StorageListRenameDialog(
    context: Context,
    private val source: Path,
    private val onResult: ItemRenameDialogResult,
) : BottomSheetDialog(
    context
) {

    private val binding = StorageListRenameBinding(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        with(binding) {
            input.requestFocus()
            input.setText(source.getName().toString())

            confirm.setOnClickListener { view ->
                if (input.text.toString() == source.getName().toString()) {
                    hide()
                }
                val newName = input.text.toString()
                val parent = source.parent!!

                onResult.invoke(parent.resolve(newName))
                hide()
            }
        }
    }

}