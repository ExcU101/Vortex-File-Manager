package io.github.excu101.vortex.ui.component.trail

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.PathItem

class TrailDiffer : DiffUtil.ItemCallback<PathItem>() {
    override fun areItemsTheSame(oldItem: PathItem, newItem: PathItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: PathItem, newItem: PathItem): Boolean {
        return oldItem == newItem
    }

}