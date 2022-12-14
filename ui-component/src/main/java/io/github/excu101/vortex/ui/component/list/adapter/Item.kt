package io.github.excu101.vortex.ui.component.list.adapter

import io.github.excu101.pluginsystem.model.DataHolder

interface Item<T> : DataHolder<T> {

    val id: Long

    val type: Int

}