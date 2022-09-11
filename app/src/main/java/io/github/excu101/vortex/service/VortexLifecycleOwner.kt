package io.github.excu101.vortex.service

interface VortexLifecycleOwner {

    enum class VortexLifecycle {
        CREATE,
        DESTROY,
        START
    }

    val lifecycle: VortexLifecycle

}