package io.github.excu101.vortex.ui.screen.storage.pager

import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageFragment

object StorageListPagerScreen {

    data class State(
        val fragments: List<StorageListPageFragment> = listOf(),
    )

    class SideEffect()
}