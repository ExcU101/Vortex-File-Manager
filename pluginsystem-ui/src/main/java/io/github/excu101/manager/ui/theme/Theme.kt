package io.github.excu101.manager.ui.theme

import io.github.excu101.manager.model.Color
import io.github.excu101.manager.model.DataHolder
import io.github.excu101.manager.model.Dimen
import io.github.excu101.manager.model.Text

object Theme {

    var isRtlEnabled: Boolean = false

    private val switcherCallbacks = mutableListOf<ThemeSwitcherCallback>()

    private val colorChangeListeners = mutableListOf<ThemeColorChangeListener>()

    private val texts: HashMap<String, Text> = hashMapOf()

    private val colors: HashMap<String, Color> = hashMapOf()

    private val dimens: HashMap<String, Dimen> = hashMapOf()

    fun getOrInsert(
        key: String,
        default: Text,
    ): Text {
        return texts[key] ?: run {
            texts[key] = default
            default
        }
    }

    fun getOrInsert(key: String, default: Color): Color {
        return colors[key] ?: run {
            set(key, default)
            default
        }
    }

    fun getOrInsert(key: String, default: Dimen): Dimen {
        return dimens[key] ?: run {
            dimens[key] = default
            default
        }
    }

    fun getText(key: String): Text =
        texts[key] ?: Text(
            String())

    inline fun getText(key: () -> String): Text =
        getText(key = key())

    fun getColor(key: String): Color = colors[key] ?: Color(
        value = -1)

    inline fun getColor(key: () -> String): Color = getColor(key = key())

    fun getDp(key: String): Dimen {
        return dimens[key] ?: Dimen(value = -1)
    }

    inline fun getDp(key: () -> String): Dimen = getDp(key = key())

    @Suppress(names = ["UNCHECKED_CAST"])
    inline operator fun <H, reified T : DataHolder<H>> get(key: String): H {
        val holder = when (T::class) {
            Color::class -> getColor(key = key)
            Dimen::class -> getDp(key = key)
            Text::class -> getText(key = key)
            else -> throw Throwable("Unsupported type")
        } as DataHolder<H>
        return holder.value
    }

    inline operator fun <H, reified T : DataHolder<H>> get(key: () -> String) = get<H, T>(key())

    inline operator fun <H, reified T : DataHolder<H>> set(key: String, holder: T) {
        when (T::class) {
            Color::class -> set(key, holder as Color)
            Dimen::class -> set(key, holder as Dimen)
            Text::class -> set(key, holder as Text)
            else -> throw Throwable("Unsupported type")
        }
    }

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
        colorChangeListeners.forEach(ThemeColorChangeListener::onColorChanged)
    }
}

