package io.github.excu101.vortex.ui.component.item.text

import android.view.View
import android.view.ViewGroup
import android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ItemViewTypes.textItem
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.theme.key.mainBarSubtitleTextColorKey

data class TextItem(
    override val value: String,
    val attrs: Attrs = attrs { },
) : Item<String> {

    data class Attrs(
        val alignment: Int,
        val color: Int,
        val size: Float,
    ) {
        class Builder {
            var alignment: Int = TEXT_ALIGNMENT_TEXT_START
            var color = ThemeColor(mainBarSubtitleTextColorKey)
            var size = 14F

            fun build() = Attrs(
                alignment = alignment,
                color = color,
                size = size
            )
        }
    }

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

inline fun ItemScope<Item<*>>.text(
    value: String,
    block: TextItem.Attrs.Builder.() -> Unit,
) {
    add(TextItem(value, attrs(block)))
}

fun ItemScope<Item<*>>.text(
    value: String,
    attrs: TextItem.Attrs = attrs { },
) {
    add(TextItem(value, attrs))
}

inline fun attrs(block: TextItem.Attrs.Builder.() -> Unit): TextItem.Attrs {
    return TextItem.Attrs.Builder().apply(block).build()
}
