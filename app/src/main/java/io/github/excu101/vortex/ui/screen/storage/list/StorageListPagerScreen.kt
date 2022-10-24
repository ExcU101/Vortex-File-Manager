package io.github.excu101.vortex.ui.screen.storage.list

import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageFragment

object StorageListPagerScreen {

    data class State(
        val fragments: List<StorageListPageFragment> = listOf(),
    )

    class SideEffect()
}