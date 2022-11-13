package io.github.excu101.pluginsystem.utils

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.*

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
    var icon: Drawable?
}

class ActionScopeImpl : ActionScope {
    override var title: String = ""
    override var icon: Drawable? = null

    fun toAction(): Action = action(title, icon)
}

interface EffectScope : ActionScope {
    var onInvoke: () -> Unit
}

@PublishedApi
internal class EffectScopeImpl : EffectScope {
    override var title: String = ""
    override var icon: Drawable? = EmptyDrawable
    override var onInvoke: () -> Unit = {}

    @PublishedApi
    internal fun toEffect(): Effect = effect(title, icon, onInvoke)
}

inline fun GroupScope.item(scope: ActionScope.() -> Unit) {
    item(ActionScopeImpl().apply(scope).toAction())
}

inline fun GroupScope.effect(scope: EffectScope.() -> Unit) {
    item(EffectScopeImpl().apply(scope).toEffect())
}

inline fun MutableList<GroupAction>.groupItem(title: String, block: GroupScope.() -> Unit) {
    add(group(title, block))
}

inline fun MutableList<Action>.action(scope: ActionScope.() -> Unit) {
    add(ActionScopeImpl().apply(scope).toAction())
}

inline fun MutableList<Action>.effect(scope: EffectScope.() -> Unit) {
    add(EffectScopeImpl().apply(scope).toEffect())
}

inline fun group(title: String, block: GroupScope.() -> Unit): GroupAction {
    return GroupScopeImpl(title).apply(block).toGroup()
}

inline fun Plugin.group(block: GroupScope.() -> Unit): GroupAction {
    return group(title = attributes.name, block = block)
}

fun GroupScope.item(title: String, icon: Drawable) {
    return item(action(title = title, icon = icon))
}

fun GroupScope.item(title: String, icon: Drawable, onInvoke: () -> Unit) {
    return item(
        effect(
            title = title,
            icon = icon,
            onInvoke = onInvoke
        )
    )
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