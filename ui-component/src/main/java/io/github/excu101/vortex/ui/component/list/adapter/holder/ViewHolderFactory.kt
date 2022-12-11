package io.github.excu101.vortex.ui.component.list.adapter.holder

import android.view.View
import android.view.ViewGroup

interface ViewHolderFactory<T> {
    fun produceView(parent: ViewGroup): View

    // Default impl
    fun produceViewHolder(child: View): ViewHolder<T> {
        return ViewHolder(child)
    }
}