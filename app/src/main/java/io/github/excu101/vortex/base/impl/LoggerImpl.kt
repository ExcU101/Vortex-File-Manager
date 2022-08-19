package io.github.excu101.vortex.base.impl

import android.util.Log
import io.github.excu101.vortex.base.Logger

class LoggerImpl(
    override val tag: String
) : Logger {

    override fun log(message: String) {
        Log.println(type, tag, message)
    }

}