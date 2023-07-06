package io.github.excu101.vortex.ui.screen.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface StateController {

    val scope: CoroutineScope

    fun onDestroy()

}

fun StateController.intent(
    block: suspend CoroutineScope.() -> Unit,
) {
    scope.launch(
        block = block
    )
}