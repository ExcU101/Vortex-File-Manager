package io.github.excu101.vortex.ui.component.list.adapter

interface SelectableItem<T> : Item<T> {

    var isSelected: Boolean

    var isSelectable: Boolean

}