package io.github.excu101.vortex.ui.component.list.adapter.dsl

import android.view.View
import android.view.ViewGroup

@DslMarker
annotation class ViewHolderMarker

const val BindEvent = 1
const val UnbindEvent = 2
const val OnAttachEvent = 3
const val OnDetachEvent = 3

@ViewHolderMarker
interface ViewHolderScope {
}

inline fun AdapterScope.viewHolder(
    type: Int,
    provider: (ViewGroup).() -> View,
    scope: ViewHolderScope.() -> Unit,
) {

}

class ViewHolderScopes {
    fun scope(block: ViewHolderScope.() -> Unit) {

    }
}

inline fun AdapterScope.viewHolders(
    count: Int,
    provider: (ViewGroup).(Int) -> View,
    vararg scopes: ViewHolderScope.() -> Unit,
) {
    for (i in 0..count) {

    }
}

inline fun AdapterScope.viewHolders(
    count: Int,
    provider: (ViewGroup).(Int) -> View,
    scopes: ViewHolderScopes.() -> Unit,
) {
    for (i in 0..count) {

    }
}


@ViewHolderMarker
interface BindingScope<V : View, I> : AdapterScope.BindingExtension {
    val view: V
    var item: I
}

inline fun <V : View, I> ViewHolderScope.bind(scope: BindingScope<V, I>.() -> Unit) {

}

@ViewHolderMarker
interface UnbindingScope<V : View> {
    val view: V
}

inline fun <V : View> ViewHolderScope.unbind(scope: UnbindingScope<V>.() -> Unit) {

}

@ViewHolderMarker
interface ListenerScope<V> {
    val view: V

    var onClick: (View.OnClickListener) -> Unit
    var onLongClick: (View.OnLongClickListener) -> Unit
}

inline fun <V : View> ViewHolderScope.listener(scope: ListenerScope<V>.() -> Unit) {

}