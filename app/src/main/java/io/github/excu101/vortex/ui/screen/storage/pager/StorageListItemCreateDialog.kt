package io.github.excu101.vortex.ui.screen.storage.pager

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.vortex.ui.component.storage.create.StorageItemCreateBinding

typealias ItemCreateDialogResult = (Path, Int, Boolean) -> Unit

class StorageListItemCreateDialog(
    context: Context,
    private val directory: Path? = null,
    private val onResult: ItemCreateDialogResult,
) : BottomSheetDialog(context) {

    private val binding: StorageItemCreateBinding = StorageItemCreateBinding(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view = binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            name.requestFocus()
            path.setText(directory.toString())

            name.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    nameLayout.error = if (name.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            path.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    pathLayout.error = if (path.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            type.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    typeLayout.error = if (type.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            mode.addTextChangedListener(
                afterTextChanged = { text: Editable? ->
                    modeLayout.error = if (mode.text?.isEmpty() != false) {
                        "To create item needs to enter item path"
                    } else {
                        null
                    }
                }
            )

            confirm.setOnClickListener { view ->
                if (name.text?.isEmpty() != false) {
                    nameLayout.error = "To create item needs to enter item name"
                    return@setOnClickListener
                }
                if (path.text?.isEmpty() != false) {
                    pathLayout.error = "To create item needs to enter item path"
                    return@setOnClickListener
                }
                if (type.text?.isEmpty() != false) {
                    typeLayout.error = "To create item needs to enter item type"
                    return@setOnClickListener
                }
                if (mode.text?.isEmpty() != false) {
                    modeLayout.error = "To create item needs to enter item type"
                    return@setOnClickListener
                }
                val name = this.name.text.toString().toPath()
                val src = this.path.text.toString().toPath()
                val path = src.resolve(name)
                val mode = this.mode.text.toString().toInt()

                onResult.invoke(
                    path,
                    mode,
                    type.text.toString() == "Directory"
                )
                hide()
            }
        }

    }

}