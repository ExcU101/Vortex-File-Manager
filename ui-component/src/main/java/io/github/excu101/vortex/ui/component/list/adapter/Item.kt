package io.github.excu101.vortex.ui.component.list.adapter

import io.github.excu101.manager.model.DataHolder

typealias SuperItem = Item<*>

interface Item<T> : DataHolder<T> {

    val id: Long

    val type: Int

}