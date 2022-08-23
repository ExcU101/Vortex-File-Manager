package io.github.excu101.pluginsystem.ui.theme

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.*
import io.github.excu101.pluginsystem.utils.EmptyDrawable

fun ThemeColor(key: String): Int {
    return Theme<Int, Color>(key)
}

fun ThemeDimen(key: String): Int {
    return Theme<Int, Dimen>(key)
}

fun ThemeIcon(key: String): Drawable {
    return Theme<Drawable, Icon>(key)
}

fun ThemeText(key: String): String {
    return Theme<String, Text>(key)
}

object Theme {

    private val icons: HashMap<String, Icon> = hashMapOf()

    private val text: HashMap<String, Text> = hashMapOf()

    private val colors: HashMap<String, Color> = hashMapOf()

    private val dimens: HashMap<String, Dimen> = hashMapOf()

    fun getText(key: String): Text = text[key] ?: Text(String())

    inline fun getText(key: () -> String): Text = getText(key = key())

    fun getColor(key: String): Color = colors[key] ?: Color(value = -1)

    inline fun getColor(key: () -> String): Color = getColor(key = key())

    fun getDp(key: String): Dimen {
        return dimens[key] ?: Dimen(value = -1)
    }

    inline fun getDp(key: () -> String): Dimen = getDp(key = key())

    fun getIcon(key: String): Icon = icons[key] ?: Icon(EmptyDrawable)

    inline fun getIcon(key: () -> String): Icon = getIcon(key())

    inline operator fun <H, reified T : DataHolder<H>> get(key: String): H {
        val holder = when (T::class) {
            Color::class -> getColor(key = key)
            Dimen::class -> getDp(key = key)
            Text::class -> getText(key = key)
            Drawable::class -> getIcon(key = key)
            else -> throw Throwable("Unsupported type")
        } as DataHolder<H>
        return holder.value
    }

    inline operator fun <H, reified T : DataHolder<H>> get(key: () -> String) = get<H, T>(key())

    operator fun set(key: String, value: Color) {
        colors[key] = value
    }

    operator fun set(key: String, value: Dimen) {
        dimens[key] = value
    }

    operator fun set(key: String, value: Text) {
        text[key] = value
    }

    operator fun set(key: String, value: Icon) {
        icons[key] = value
    }
}

inline fun <H, reified T : DataHolder<H>> Theme(key: String) = Theme.get<H, T>(key)

@Suppress(names = ["FunctionName"])
inline fun <H, reified T : DataHolder<H>> Theme(block: () -> String) = Theme.get<H, T>(block)