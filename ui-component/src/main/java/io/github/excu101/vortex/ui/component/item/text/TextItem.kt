package io.github.excu101.vortex.ui.component.item.text

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView.TEXT_ALIGNMENT_TEXT_START
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextItem(
    override val value: String,
    val attrs: Attrs = attrs { },
) : Item<String>, Parcelable {

    @Parcelize
    data class Attrs(
        val alignment: Int,
        val color: Int,
        val size: Float,
    ) : Parcelable {
        class Builder {
            var alignment: Int = TEXT_ALIGNMENT_TEXT_START
            var color =
                io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey)
            var size = 14F

            fun build() = Attrs(
                alignment = alignment,
                color = color,
                size = size
            )
        }
    }

    @IgnoredOnParcel
    override val id: Long = hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.TextItem

    companion object : ViewHolderFactory<TextItem> {
        override fun produceView(parent: ViewGroup): View {
            return TextItemView(parent.context)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextItem

        if (value != other.value) return false
        if (attrs != other.attrs) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

inline fun ItemScope<Item<*>>.text(
    value: String,
    block: TextItem.Attrs.Builder.() -> Unit = { },
) {
    add(TextItem(value, attrs(block)))
}

inline fun attrs(block: TextItem.Attrs.Builder.() -> Unit): TextItem.Attrs {
    return TextItem.Attrs.Builder().apply(block).build()
}
