package io.github.excu101.vortex.ui.component.trail

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class TrailViewHolder(private val root: TrailItemView) : ViewHolder<PathItem>(root) {

    var isArrowVisible: Boolean
        get() = root.isArrowVisible
        set(value) {
            root.isArrowVisible = value
        }

}