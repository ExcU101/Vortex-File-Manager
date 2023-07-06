package io.github.excu101.vortex.service.notification

import io.github.excu101.filesystem.fs.path.Path

data class VortexNotification(
    var progress: Int,
    var max: Int,
    var current: Path? = null,
) {

    val isFinished: Boolean
        get() = progress == max

}