package io.github.excu101.pluginsystem.ui.theme

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import io.github.excu101.pluginsystem.model.*
import io.github.excu101.pluginsystem.utils.EmptyDrawable

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

fun ReplacerThemeText(key: String, old: String, new: String): String {
    return ThemeText(key).replace(old, new)
}

fun ThemeText(key: String, default: Text): String {
    return Theme.getOrReplace(key, default).value
}

object Theme {

    var isRtlEnabled: Boolean = false

    private val switcherCallbacks = mutableListOf<ThemeSwitcherCallback>()

    private val colorChangeListeners = mutableListOf<ThemeColorChangeListener>()

    private val icons: HashMap<String, Icon> = hashMapOf()

    private val texts: HashMap<String, Text> = hashMapOf()

    private val colors: HashMap<String, Color> = hashMapOf()

    private val dimens: HashMap<String, Dimen> = hashMapOf()

    fun getOrReplace(key: String, default: Text): Text {
        return texts[key] ?: run {
            texts[key] = default
            default
        }
    }

    fun getOrReplace(key: String, default: Color): Color {
        return colors[key] ?: run {
            set(key, default)
            default
        }
    }

    fun getOrReplace(key: String, default: Icon): Icon {
        return icons[key] ?: run {
            icons[key] = default
            default
        }
    }

    fun getOrReplace(key: String, default: Dimen): Dimen {
        return dimens[key] ?: run {
            dimens[key] = default
            default
        }
    }

    fun getText(key: String): Text = texts[key] ?: Text(String())

    inline fun getText(key: () -> String): Text = getText(key = key())

    fun getColor(key: String): Color = colors[key] ?: Color(value = -1)

    inline fun getColor(key: () -> String): Color = getColor(key = key())

    fun getDp(key: String): Dimen {
        return dimens[key] ?: Dimen(value = -1)
    }

    inline fun getDp(key: () -> String): Dimen = getDp(key = key())

    fun getIcon(key: String): Icon = icons[key] ?: Icon(EmptyDrawable)

    inline fun getIcon(key: () -> String): Icon = getIcon(key())

    @Suppress(names = ["UNCHECKED_CAST"])
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
        notifyColorsChanged()
    }

    operator fun set(key: String, value: Dimen) {
        dimens[key] = value
    }

    operator fun set(key: String, value: Text) {
        texts[key] = value
    }

    operator fun set(key: String, value: Icon) {
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