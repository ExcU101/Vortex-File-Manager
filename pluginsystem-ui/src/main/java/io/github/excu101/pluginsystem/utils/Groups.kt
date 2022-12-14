package io.github.excu101.pluginsystem.utils

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction

interface GroupScope {
    var title: String
    var icon: Drawable?
    val items: Collection<Action>

    fun item(action: Action)
}


class GroupScopeImpl(override var title: String) : GroupScope {

    override var icon: Drawable? = null

    private val _items = mutableListOf<Action>()

    override val items: Collection<Action>
        get() = _items

    override fun item(action: Action) {
        _items.add(action)
    }


    fun toGroup(): GroupAction =
        GroupAction(
            name = title,
            icon = icon,
            actions = items
        )
}

interface ActionScope {
    var title: String
    var icon: Drawable?
}

class ActionScopeImpl : ActionScope {
    override var title: String = ""
    override var icon: Drawable? = null

    fun toAction(): Action =
        io.github.excu101.pluginsystem.model.action(title, icon)
}

inline fun GroupScope.item(scope: ActionScope.() -> Unit) {
    item(ActionScopeImpl().apply(scope).toAction())
}

inline fun MutableList<GroupAction>.groupItem(
    title: String,
    block: GroupScope.() -> Unit,
) {
    add(group(title, block))
}

inline fun MutableList<Action>.action(scope: ActionScope.() -> Unit) {
    add(ActionScopeImpl().apply(scope).toAction())
}

inline fun group(
    title: String,
    block: GroupScope.() -> Unit,
): GroupAction {
    return GroupScopeImpl(title).apply(block).toGroup()
}

fun GroupScope.item(title: String, icon: Drawable) {
    return item(io.github.excu101.pluginsystem.model.action(title = title, icon = icon))
}

fun List<GroupAction>.search(action: Action): GroupAction? {
    val group: GroupAction? = null

    return fold(group) { prev, current ->
        if (current.actions.contains(action)) {
            current
        } else {
            prev
        }
    }
}