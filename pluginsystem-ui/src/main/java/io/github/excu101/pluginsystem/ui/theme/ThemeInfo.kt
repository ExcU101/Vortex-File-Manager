package io.github.excu101.pluginsystem.ui.theme

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.model.DataHolder
import io.github.excu101.pluginsystem.model.Icon
import io.github.excu101.pluginsystem.model.Text

abstract class ThemeInfo<T : DataHolder<*>> {
    abstract val name: String
    abstract val keys: List<String>
}

class ThemeColorInfo(
    override val name: String,
    override val keys: List<String>,
) : ThemeInfo<Color>()

class ThemeDimenInfo(
    override val name: String,
    override val keys: List<String>,
) : ThemeInfo<Text>()

class ThemeIconInfo(
    override val name: String,
    override val keys: List<String>,
) : ThemeInfo<Icon>()

class ThemeTextInfo(
    override val name: String,
    override val keys: List<String>,
) : ThemeInfo<Text>()