package io.github.excu101.pluginsystem.ui.theme

import android.graphics.drawable.Drawable
import io.github.excu101.pluginsystem.model.*
import io.github.excu101.pluginsystem.utils.EmptyDrawable

fun ThemeColor(key: String): Int {
    return Theme<Int, io.github.excu101.pluginsystem.model.Color>(key)
}

fun ThemeColor(key: String, default: io.github.excu101.pluginsystem.model.Color): Int {
    return Theme.getOrReplace(key, default).value
}

fun ThemeDimen(key: String): Int {
    return Theme<Int, io.github.excu101.pluginsystem.model.Dimen>(key)
}

fun ThemeDimen(key: String, default: io.github.excu101.pluginsystem.model.Dimen): Int {
    return Theme.getOrReplace(key, default).value
}

fun ThemeIcon(key: String): Drawable {
    return Theme<Drawable, io.github.excu101.pluginsystem.model.Icon>(key)
}

fun ThemeIcon(key: String, default: io.github.excu101.pluginsystem.model.Icon): Drawable {
    return Theme.getOrReplace(key, default).value
}

fun ThemeText(key: String): String {
    return Theme<String, io.github.excu101.pluginsystem.model.Text>(key)
}

fun FormatterThemeText(key: String, vararg values: Any?): String {
    return ThemeText(key).format(*values)
}

fun ThemeText(key: String, default: io.github.excu101.pluginsystem.model.Text): String {
    return Theme.getOrReplace(key, default).value
}

object Theme {

    var isRtlEnabled: Boolean = false

    private val switcherCallbacks = mutableListOf<ThemeSwitcherCallback>()

    private val colorChangeListeners = mutableListOf<ThemeColorChangeListener>()

    private val icons: HashMap<String, io.github.excu101.pluginsystem.model.Icon> = hashMapOf()

    private val texts: HashMap<String, io.github.excu101.pluginsystem.model.Text> = hashMapOf()

    private val colors: HashMap<String, io.github.excu101.pluginsystem.model.Color> = hashMapOf()

    private val dimens: HashMap<String, io.github.excu101.pluginsystem.model.Dimen> = hashMapOf()

    fun getOrReplace(key: String, default: io.github.excu101.pluginsystem.model.Text): io.github.excu101.pluginsystem.model.Text {
        return texts[key] ?: run {
            texts[key] = default
            default
        }
    }

    fun getOrReplace(key: String, default: io.github.excu101.pluginsystem.model.Color): io.github.excu101.pluginsystem.model.Color {
        return colors[key] ?: run {
            set(key, default)
            default
        }
    }

    fun getOrReplace(key: String, default: io.github.excu101.pluginsystem.model.Icon): io.github.excu101.pluginsystem.model.Icon {
        return icons[key] ?: run {
            icons[key] = default
            default
        }
    }

    fun getOrReplace(key: String, default: io.github.excu101.pluginsystem.model.Dimen): io.github.excu101.pluginsystem.model.Dimen {
        return dimens[key] ?: run {
            dimens[key] = default
            default
        }
    }

    fun getText(key: String): io.github.excu101.pluginsystem.model.Text = texts[key] ?: io.github.excu101.pluginsystem.model.Text(
        String())

    inline fun getText(key: () -> String): io.github.excu101.pluginsystem.model.Text = getText(key = key())

    fun getColor(key: String): io.github.excu101.pluginsystem.model.Color = colors[key] ?: io.github.excu101.pluginsystem.model.Color(
        value = -1)

    inline fun getColor(key: () -> String): io.github.excu101.pluginsystem.model.Color = getColor(key = key())

    fun getDp(key: String): io.github.excu101.pluginsystem.model.Dimen {
        return dimens[key] ?: io.github.excu101.pluginsystem.model.Dimen(value = -1)
    }

    inline fun getDp(key: () -> String): io.github.excu101.pluginsystem.model.Dimen = getDp(key = key())

    fun getIcon(key: String): io.github.excu101.pluginsystem.model.Icon = icons[key] ?: io.github.excu101.pluginsystem.model.Icon(
        EmptyDrawable)

    inline fun getIcon(key: () -> String): io.github.excu101.pluginsystem.model.Icon = getIcon(key())

    @Suppress(names = ["UNCHECKED_CAST"])
    inline operator fun <H, reified T : DataHolder<H>> get(key: String): H {
        val holder = when (T::class) {
            io.github.excu101.pluginsystem.model.Color::class -> getColor(key = key)
            io.github.excu101.pluginsystem.model.Dimen::class -> getDp(key = key)
            io.github.excu101.pluginsystem.model.Text::class -> getText(key = key)
            io.github.excu101.pluginsystem.model.Icon::class -> getIcon(key = key)
            else -> throw Throwable("Unsupported type")
        } as DataHolder<H>
        return holder.value
    }

    inline operator fun <H, reified T : DataHolder<H>> get(key: () -> String) = get<H, T>(key())

    inline operator fun <H, reified T : DataHolder<H>> set(key: String, holder: T) {
        when (T::class) {
            io.github.excu101.pluginsystem.model.Color::class -> set(key, holder as io.github.excu101.pluginsystem.model.Color)
            io.github.excu101.pluginsystem.model.Dimen::class -> set(key, holder as io.github.excu101.pluginsystem.model.Dimen)
            io.github.excu101.pluginsystem.model.Text::class -> set(key, holder as io.github.excu101.pluginsystem.model.Text)
            io.github.excu101.pluginsystem.model.Icon::class -> set(key, holder as io.github.excu101.pluginsystem.model.Icon)
            else -> throw Throwable("Unsupported type")
        }
    }

    operator fun set(key: String, value: io.github.excu101.pluginsystem.model.Color) {
        colors[key] = value
        notifyColorsChanged()
    }

    operator fun set(key: String, value: io.github.excu101.pluginsystem.model.Dimen) {
        dimens[key] = value
    }

    operator fun set(key: String, value: io.github.excu101.pluginsystem.model.Text) {
        texts[key] = value
    }

    operator fun set(key: String, value: io.github.excu101.pluginsystem.model.Icon) {
        icons[key] = value
    }

    fun registerColorChangeListener(listener: ThemeColorChangeListener) {
        colorChangeListeners.add(listener)
    }

    fun unregisterColorChangeListener(listener: ThemeColorChangeListener) {
        colorChangeListeners.remove(listener)
    }

    fun attachCallback(callback: ThemeSwitcherCallback) {
        switcherCallbacks.add(callback)
    }

    fun detachCallback(callback: ThemeSwitcherCallback) {
        switcherCallbacks.remove(callback)
    }

    fun switch() {
        switcherCallbacks.forEach(ThemeSwitcherCallback::onSwitch)
    }

    fun notifyColorsChanged() {
        colorChangeListeners.forEach(ThemeColorChangeListener::onChanged)
    }
}

inline fun <H, reified T : DataHolder<H>> Theme(key: String) = Theme.get<H, T>(key)

@Suppress(names = ["FunctionName"])
inline fun <H, reified T : DataHolder<H>> Theme(block: () -> String) = Theme.get<H, T>(block)