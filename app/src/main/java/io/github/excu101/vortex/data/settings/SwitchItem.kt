package io.github.excu101.vortex.data.settings

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.settings.SettingSwitchCell
import io.github.excu101.vortex.utils.SwitchItem

class SwitchItem(
    override val value: String
) : Item<String> {

    override val type: Int
        get() = ItemViewTypes.SwitchItem

    override val id: Long = value.hashCode().toLong()

    companion object : ViewHolderFactory<SwitchItem> {
        override fun produceView(parent: ViewGroup): View = SettingSwitchCell(parent.context)
    }

}