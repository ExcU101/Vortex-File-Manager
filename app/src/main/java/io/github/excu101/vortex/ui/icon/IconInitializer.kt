package io.github.excu101.vortex.ui.icon

import android.content.Context

object IconInitializer {

    var context: Context? = null
        get() = field ?: throw IllegalArgumentException()
        set(value) {
            field = value?.applicationContext
        }

    operator fun get(id: Int) = context!!.getDrawable(id)

}