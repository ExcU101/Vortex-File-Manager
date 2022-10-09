package io.github.excu101.vortex.utils

import io.github.excu101.vortex.BuildConfig

object Config {

    val isDebug
        get() = BuildConfig.DEBUG

    val version: String
        get() = BuildConfig.VERSION_NAME


}