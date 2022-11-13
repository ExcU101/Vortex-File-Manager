package io.github.excu101.vortex.service.utils

import android.net.Uri
import io.github.excu101.filesystem.fs.path.Path

private const val scheme = "content"
private const val specific = "vortex.service"

fun Path.toVortexServiceUri(): Uri {
    return Uri.fromParts(scheme, specific, toString())
}