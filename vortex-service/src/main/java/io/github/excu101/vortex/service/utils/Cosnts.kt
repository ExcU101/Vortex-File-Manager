package io.github.excu101.vortex.service.utils

import android.content.Context

const val VORTEX_SERVICE_ACTION_NAME = "io.github.excu101.VORTEX_SERVICE"

val Context.PLUGIN_DEST
    get() = "$filesDir/plugin"