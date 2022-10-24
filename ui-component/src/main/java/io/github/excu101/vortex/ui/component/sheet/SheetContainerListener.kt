package io.github.excu101.vortex.ui.component.sheet

import io.github.excu101.vortex.ui.component.foundtation.Listener

interface SheetContainerListener : Listener {

    fun onScrollAccepted()

    fun onNestedPreScroll(dx: Int, dy: Int, consumed: IntArray)

}