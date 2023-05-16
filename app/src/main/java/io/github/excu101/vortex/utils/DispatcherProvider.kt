package io.github.excu101.vortex.utils

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

class DispatcherProvider @Inject constructor() {
    val io = Dispatchers.IO
    val main = Dispatchers.Main
    val default = Dispatchers.Default

    val StorageStateUpdateCoroutineScope = CoroutineScope(
        CoroutineName(StorageStateUpdateScope) + SupervisorJob() + main.immediate
    )
}