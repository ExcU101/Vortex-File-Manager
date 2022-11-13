package io.github.excu101.vortex.ui.screen.storage.pager.page.bookmark

import io.github.excu101.vortex.ui.component.list.adapter.Item

object StorageBookmarkScreen {

    data class State(
        val data: List<Item<*>> = listOf(),
    )

    class SideEffect(

    )

}