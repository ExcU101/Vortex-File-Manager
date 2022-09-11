package io.github.excu101.vortex.ui.component.list.adapter

import androidx.recyclerview.widget.DiffUtil

interface EditableAdapter<T> {

    operator fun get(index: Int): T = item(index)

    operator fun get(item: T): Int = position(item)

    fun replace(items: List<T>, differ: DiffUtil.Callback?)

    fun add(item: T)

    fun add(position: Int, item: T)

    fun add(items: Iterable<T>)

    fun remove(item: T)

    fun remove(position: Int)

    fun position(item: T): Int

    fun item(position: Int): T

}