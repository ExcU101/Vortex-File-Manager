package io.github.excu101.vortex.ui.component.header.text

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

data class TextHeaderItem(
    override val value: String,
) : Item<String> {

    override val id: Long
        get() = value.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.TEXT_HEADER

    companion object : ViewHolderFactory<TextHeaderItem> {
        override fun produceView(parent: ViewGroup): View {
            return TextHeaderView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<TextHeaderItem> {
            return TextHeaderViewHolder(child as TextHeaderView)
        }
    }
}

fun ItemScope<Item<*>>.text(value: String) {
    add(TextHeaderItem(value))
}
