package io.github.excu101.pluginsystem.ui.theme

import io.github.excu101.pluginsystem.model.Color

abstract class ThemeInfo<T> {
    abstract val name: String
    abstract val pairs: Map<String, T>

    val values: Collection<T>
        get() = pairs.values

    val keys: Collection<String>
        get() = pairs.keys
}

class ColorInfo : ThemeInfo<Color>() {

    override val name: String
        get() = ""

    private val _pairs = mutableMapOf<String, Color>()

    override val pairs: Map<String, Color>
        get() = _pairs

}