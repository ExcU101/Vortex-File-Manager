package io.github.excu101.vortex.ui.component.item.info

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

class InfoItem(
    override val value: Info,
) : Item<Info> {

    constructor(value: String, description: String) : this(Info(value, description))

    override val id: Long = value.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.infoItem

    companion object : ViewHolderFactory<InfoItem> {
        override fun produceView(parent: ViewGroup): View {
            return InfoItemView(parent.context)
        }
    }

}

fun ItemScope<Item<*>>.info(value: String, description: String) {
    add(InfoItem(value, description))
}
