package io.github.excu101.vortex.ui.component.list.adapter.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup

fun interface ViewHolderFactory<T> {
    fun produceView(parent: ViewGroup): View

    // Default impl
    fun produceViewHolder(child: View): ViewHolder<T> {
        return ViewHolder(child)
    }
}

fun <T> contextViewHolderFactory(
    produce: (Context) -> View
) = ViewHolderFactory<T> { parent ->
    produce(parent.context)
}