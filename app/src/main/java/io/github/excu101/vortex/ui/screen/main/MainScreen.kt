package io.github.excu101.vortex.ui.screen.main

import androidx.fragment.app.Fragment

object MainScreen {

    data class State(
        val fragment: Fragment,
        val tag: String? = null,
    )

    class SideEffect()

}