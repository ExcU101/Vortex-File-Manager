package io.github.excu101.vortex.ui.component.trail

import androidx.recyclerview.widget.DiffUtil
import io.github.excu101.vortex.data.TrailItem

object TrailDiffer : DiffUtil.ItemCallback<TrailItem>() {
    override fun areItemsTheSame(oldItem: TrailItem, newItem: TrailItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrailItem, newItem: TrailItem): Boolean {
        return oldItem == newItem
    }

}