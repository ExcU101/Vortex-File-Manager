package io.github.excu101.vortex.ui.screen.settings

import io.github.excu101.vortex.ui.component.list.adapter.Item

object SettingPageScreen {

    data class State(
        val content: List<Item<*>> = emptyList()
    )

    class SideEffect()

}