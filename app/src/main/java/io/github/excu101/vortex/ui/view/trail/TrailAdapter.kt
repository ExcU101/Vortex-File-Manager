package io.github.excu101.vortex.ui.view.trail

import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.base.BaseAdapter

class TrailAdapter : BaseAdapter<TrailItem, TrailViewHolder>(
    differ = TrailDiffer()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        return TrailViewHolder(root = TrailItemView(context = parent.context))
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.setOnClickListener { view ->
            notify(view = view, value = item, position = position)
        }
    }
}