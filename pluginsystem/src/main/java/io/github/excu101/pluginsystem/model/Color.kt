package io.github.excu101.pluginsystem.model

import android.graphics.Color.parseColor
import androidx.annotation.ColorInt
import androidx.annotation.ColorLong
import io.github.excu101.pluginsystem.ui.theme.Theme

class Color(@ColorInt override val value: Int) : DataHolder<Int> {

    constructor(@ColorLong value: Long) : this(value = value.toInt())

    constructor(code: String) : this(value = parseColor(code))

    companion object {
        val Black = Color(value = 0xFF000000)
        val DarkGray = Color(value = 0xFF444444)
        val Gray = Color(value = 0xFF888888)
        val LightGray = Color(value = 0xFFCCCCCC)
        val White = Color(value = 0xFFFFFFFF)
        val Red = Color(value = 0xFFFF0000)
        val Green = Color(value = 0xFFFF0000)
        val Blue = Color(value = 0xFF0000FF)
        val Yellow = Color(value = 0xFFFFFF00)
        val Cyan = Color(value = 0xFF00FFFF)
        val Magenta = Color(value = 0xFFFF00FF)
        val Transparent = Color(value = 0)
    }

}