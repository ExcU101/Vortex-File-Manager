package io.github.excu101.vortex.ui.view.trail

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.TrailItem

class TrailDiffer : DiffUtil.ItemCallback<TrailItem>() {
    override fun areItemsTheSame(oldItem: TrailItem, newItem: TrailItem): Boolean {
        return oldItem.value.path == newItem.value.path
    }

    override fun areContentsTheSame(oldItem: TrailItem, newItem: TrailItem): Boolean {
        return oldItem == newItem
    }

}