package io.github.excu101.vortex.provider.settings

import io.github.excu101.vortex.ui.component.list.adapter.Item

class Setting<T>(
    val name: String,
    override val value: T
) : Item<T> {

    override val id: Long = (name.hashCode() + value.hashCode()).toLong()
    override val type: Int
        get() = 0

}