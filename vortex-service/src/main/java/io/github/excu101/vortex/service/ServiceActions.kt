package io.github.excu101.vortex.service

object ServiceActions {

    object Music {
        const val Start = "start_vortex_music_service"
        const val Stop = "stop_vortex_music_service"
    }

    object Operation {
        const val CANCEL = "cancel_vortex_path_operation"
        const val CANCEL_OBSERVE = "cancel_vortex_path_observation"

        const val DELETE = "vortex"
    }

    val Start = "start_vortex_service"
    val Stop = "stop_vortex_service"

}