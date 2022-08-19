package io.github.excu101.vortex.data

import androidx.annotation.ColorInt
import androidx.annotation.ColorLong

class Color(@ColorInt override val value: Int) : DataHolder<Int> {

    constructor(@ColorLong value: Long) : this(value = value.toInt())

    constructor(name: String) : this(value = android.graphics.Color.parseColor(name))

    companion object {
        val Black = Color(value = 0xFF000000)
        val DarkGray = Color(value = 0xFF444444)
        val Gray = Color(value = 0xFF888888.toInt())
        val LightGray = Color(value = 0xFFCCCCCC)
        val White = Color(value = 0xFFFFFFFF)
        val Red = Color(value = 0xFFFF0000)
        val Transparent = Color(value = 0)
    }

}