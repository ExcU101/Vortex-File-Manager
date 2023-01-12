package io.github.excu101.vortex.ui.component.item.drawer

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.menu.MenuAction

data class DrawerItem(
    override val value: MenuAction,
    val attrs: Attrs = Attrs()
) : Item<MenuAction> {

    data class Attrs(
        @ColorInt
        val textColor: Int? = null,
        @ColorInt
        val iconColor: Int? = null
    ) {
        constructor(@ColorInt color: Int? = null) : this(textColor = color, iconColor = color)
    }

    constructor(
        id: Int,
        title: String,
        icon: Drawable,
        attrs: Attrs = Attrs()
    ) : this(MenuAction(id, title, icon), attrs)

    override val id: Long = value.id.toLong()

    override val type: Int
        get() = ItemViewTypes.DrawerItem

    companion object : ViewHolderFactory<DrawerItem> {
        override fun produceView(parent: ViewGroup): View {
            return DrawerItemView(parent.context)
        }
    }
}

fun ItemScope<Item<*>>.drawerItem(value: MenuAction) {
    add(DrawerItem(value))
}

interface DrawerItemScope {
    var id: Int
    var title: String
    var icon: Drawable?
    var attrs: DrawerItem.Attrs

    interface AttrsScope {
        @setparam:ColorInt
        var textColor: Int?

        @setparam:ColorInt
        var iconColor: Int?
    }
}

class DrawerItemScopeImpl(
    override var id: Int = -1,
    override var title: String = "",
    override var icon: Drawable? = null,
    override var attrs: DrawerItem.Attrs = DrawerItem.Attrs()
) : DrawerItemScope {

    class AttrsScopeImpl(
        @ColorInt
        override var textColor: Int? = null,
        @ColorInt
        override var iconColor: Int? = null
    ) : DrawerItemScope.AttrsScope {
        fun toAttrs() = DrawerItem.Attrs(textColor, iconColor)
    }

    fun toAction() = MenuAction(id, title, icon)

    fun toItem() = DrawerItem(toAction(), attrs)
}

inline fun ItemScope<Item<*>>.drawerItem(
    scope: DrawerItemScope.() -> Unit,
) {

    add(DrawerItemScopeImpl().apply(scope).toItem())
}

fun DrawerItemScope.attrs(
    scope: DrawerItemScope.AttrsScope.() -> Unit
) {
    attrs = DrawerItemScopeImpl.AttrsScopeImpl().apply(scope).toAttrs()
}