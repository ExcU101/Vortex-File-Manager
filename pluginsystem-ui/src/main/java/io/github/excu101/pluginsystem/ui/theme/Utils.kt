package io.github.excu101.pluginsystem.ui.theme

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.*

fun ThemeColor(key: String): Int {
    return Theme<Int, Color>(key)
}

fun ThemeColor(key: String, default: Color): Int {
    return Theme.getOrReplace(key, default).value
}

fun ThemeDimen(key: String): Int {
    return Theme<Int, Dimen>(key)
}

fun ThemeDimen(key: String, default: Dimen): Int {
    return Theme.getOrReplace(key, default).value
}

fun ThemeIcon(key: String): Drawable {
    return Theme<Drawable, Icon>(key)
}

fun ThemeIcon(key: String, default: Icon): Drawable {
    return Theme.getOrReplace(key, default).value
}

fun ThemeText(key: String): String {
    return Theme<String, Text>(key)
}

fun FormatterThemeText(key: String, vararg values: Any?): String {
    return ThemeText(key).format(*values)
}

fun ThemeText(key: String, default: Text): String {
    return Theme.getOrReplace(key, default).value
}

inline fun <H, reified T : DataHolder<H>> Theme(key: String) = Theme.get<H, T>(key)

@Suppress(names = ["FunctionName"])
inline fun <H, reified T : DataHolder<H>> Theme(block: () -> String) = Theme.get<H, T>(block)