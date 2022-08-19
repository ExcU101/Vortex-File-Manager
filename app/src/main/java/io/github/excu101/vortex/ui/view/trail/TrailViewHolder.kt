package io.github.excu101.vortex.ui.view.trail

import android.view.View
import io.github.excu101.vortex.data.TrailItem
import io.github.excu101.vortex.ui.base.BaseViewHolder

class TrailViewHolder(private val root: TrailItemView) : BaseViewHolder<TrailItem>(root) {

    override fun bind(value: TrailItem) {
        root.setTitle(value = value.value.path.getName().toString())
        root.setArrowVisibility(!value.isLast)
        root.updateSelection(value.isSelected)
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        root.setOnClickListener(listener)
    }

}