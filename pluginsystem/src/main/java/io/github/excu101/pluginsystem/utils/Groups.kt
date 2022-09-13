package io.github.excu101.pluginsystem.utils

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin

interface GroupScope {
    var title: String
    var icon: Drawable?
    val items: Collection<Action>

    fun item(action: Action)
}

@PublishedApi
internal class GroupScopeImpl(override var title: String) : GroupScope {

    override var icon: Drawable? = null

    private val _items = mutableListOf<Action>()

    override val items: Collection<Action>
        get() = _items

    override fun item(action: Action) {
        _items.add(action)
    }

    @PublishedApi
    internal fun toGroup(): GroupAction = GroupAction(
        name = title,
        icon = icon,
        actions = items
    )
}

interface ActionScope {
    var title: String
    var icon: Drawable
}

@PublishedApi
internal class ActionScopeImpl : ActionScope {
    override var title: String = ""
    override var icon: Drawable = EmptyDrawable

    @PublishedApi
    internal fun toAction(): Action = Action(title, icon)
}

inline fun GroupScope.item(scope: ActionScope.() -> Unit) {
    item(ActionScopeImpl().apply(scope).toAction())
}

inline fun MutableList<GroupAction>.groupItem(title: String, block: GroupScope.() -> Unit) {
    add(group(title, block))
}

inline fun group(title: String, block: GroupScope.() -> Unit): GroupAction {
    return GroupScopeImpl(title).apply(block).toGroup()
}

inline fun Plugin.group(block: GroupScope.() -> Unit): GroupAction {
    return group(title = attributes.name, block = block)
}

fun GroupScope.item(title: String, icon: Drawable) {
    return item(Action(title = title, icon = icon))
}