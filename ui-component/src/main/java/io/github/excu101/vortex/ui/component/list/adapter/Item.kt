package io.github.excu101.vortex.ui.component.list.adapter

import io.github.excu101.vortex.theme.model.DataHolder

typealias SuperItem = Item<*>

interface Item<T> : io.github.excu101.vortex.theme.model.DataHolder<T> {

    val id: Long

    val type: Int

}