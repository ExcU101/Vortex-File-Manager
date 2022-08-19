package io.github.excu101.vortex.ui.view.storage

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.filesystem.fs.FileUnit

class StorageUnitDiffer : DiffUtil.ItemCallback<FileUnit>() {
    override fun areItemsTheSame(oldItem: FileUnit, newItem: FileUnit): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: FileUnit, newItem: FileUnit): Boolean {
        return oldItem == newItem
    }
}