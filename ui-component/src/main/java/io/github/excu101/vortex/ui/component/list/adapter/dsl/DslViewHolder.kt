package io.github.excu101.vortex.ui.component.list.adapter.dsl

import android.view.View
import android.view.ViewGroup
import io.github.excu101.vortex.ui.component.list.adapter.dsl.ViewHolderScope.ListenerScope
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory

class DslViewHolder<T, V : View>(root: V) : ViewHolder<T>(root), ViewHolderScope<T, V> {

    internal var onBind: (T) -> Unit = { _ -> }
    internal val onUnbind: (View) -> Unit = {}
    internal val onBindListener: (View) -> Unit = {}

    override fun bind(item: T) {
        onBind(item)
    }

    override fun bindListener(listener: View.OnClickListener) {
        super.bindListener(listener)
    }

    override val view: V = root

}

interface ViewHolderScope<T, V : View> {
    val view: V

    interface ListenerScope
}

inline fun <T, V : View> ItemAdapterScope<T>.withHolder(
    viewType: Int,
    factory: ViewHolderFactory<T>,
    scope: ViewHolderScope<T, V>.() -> Unit,
) {
    withViewType(viewType, factory)
}

inline fun <T, V : View> ItemAdapterScope<T>.withHolder(
    viewType: Int,
    crossinline view: (ViewGroup) -> V,
    scope: ViewHolderScope<T, V>.() -> Unit,
) = withHolder(
    viewType = viewType,
    factory = object : ViewHolderFactory<T> {
        override fun produceView(parent: ViewGroup): View {
            return view(parent)
        }
    },
    scope = scope,
)

inline fun <T, reified V : View> ItemAdapterScope<T>.withHolder(
    viewType: Int,
    scope: ViewHolderScope<T, V>.() -> Unit,
) = withHolder(
    viewType = viewType,
    view = { V::class.constructors.first().call(it.context) },
    scope = scope,
)

fun <T, V : View> ViewHolderScope<T, V>.bind(block: (value: T) -> Unit) {
    (this as DslViewHolder<T, V>).onBind = block
}

fun <T, V : View> ViewHolderScope<T, V>.bind(recyclable: ViewHolder.RecyclableView<T>) {
    (this as DslViewHolder<T, V>).onBind = { recyclable.onBind(it) }
}


inline fun <L : ListenerScope> ViewHolderScope<*, *>.listener(block: L.() -> Unit) {

}