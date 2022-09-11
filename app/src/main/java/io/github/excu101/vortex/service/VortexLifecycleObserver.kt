package io.github.excu101.vortex.service

import io.github.excu101.vortex.service.VortexLifecycleOwner.VortexLifecycle

fun interface VortexLifecycleObserver {

    fun onChange(event: VortexLifecycle)

}