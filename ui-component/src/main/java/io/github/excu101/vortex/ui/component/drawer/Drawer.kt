package io.github.excu101.vortex.ui.component.drawer

interface Drawer {

    val isOpen: Boolean

    val isClosed: Boolean

    fun show()

    fun hide()

    fun toggle()

}