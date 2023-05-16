package io.github.excu101.vortex.ui.icon

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources

object IconInitializer {

    var context: Context? = null
        get() = field ?: throw IllegalArgumentException()
        set(value) {
            field = value?.applicationContext
        }

    operator fun get(id: Int) = AppCompatResources.getDrawable(context!!, id)

}