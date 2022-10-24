package io.github.excu101.vortex.ui.component.item.text

import android.view.View
import android.view.ViewGroup
import android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
import io.github.excu101.vortex.ui.component.ItemViewTypes.textItem
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory

data class TextItem(
    override val value: String,
    val alignment: Int = TEXT_ALIGNMENT_TEXT_START,
) : Item<String> {

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = textItem

    companion object : ViewHolderFactory<TextItem> {
        override fun produceView(parent: ViewGroup): View {
            return TextItemView(parent.context)
        }
    }
}

fun ItemScope<Item<*>>.text(value: String) {
    add(TextItem(value))
}
