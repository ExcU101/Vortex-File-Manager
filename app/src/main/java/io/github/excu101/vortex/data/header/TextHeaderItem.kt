package io.github.excu101.vortex.data.header

import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item

data class TextHeaderItem(
    override val value: String,
) : Item<String> {

    override val id: Long
        get() = value.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.TEXT_HEADER

}