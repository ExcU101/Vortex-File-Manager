package io.github.excu101.vortex.ui.theme

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.data.DataHolder
import io.github.excu101.vortex.data.Dimen
import io.github.excu101.vortex.data.Text

object Theme {

    val light by lazy {
        isDark = false
    }

    val dark by lazy {
        isDark = true
    }

    private val icons: HashMap<String, Drawable> = hashMapOf()

    private val text: HashMap<String, Text> = hashMapOf()

    private val colors: HashMap<String, Color> = hashMapOf()

    private val dimens: HashMap<String, Dimen> = hashMapOf()

    var isDark: Boolean = false
        set(value) {
            field = value
            update()
        }

    fun getText(key: String): Text = text[key] ?: Text(String())

    inline fun getText(key: () -> String): Text = getText(key = key())

    fun getColor(key: String): Color = colors[key] ?: Color(value = -1)

    inline fun getColor(key: () -> String): Color = getColor(key = key())

    fun getDp(key: String): Dimen {
        return dimens[key] ?: Dimen(value = -1)
    }

    inline fun getDp(key: () -> String): Dimen = getDp(key = key())

    fun getIcon(key: String): Drawable = icons[key] ?: ShapeDrawable()

    inline fun getIcon(key: () -> String): Drawable = getIcon(key())

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

    operator fun set(key: String, value: Drawable) {
        icons[key] = value
    }

    init {
        update()
    }

    fun update() {
        if (isDark) defaultDarkTheme() else defaultLightTheme()
        defaultDimens()
        defaultTexts()
    }
}

inline fun <H, reified T : DataHolder<H>> Theme(key: String) = Theme.get<H, T>(key)

@Suppress(names = ["FunctionName"])
inline fun <H, reified T : DataHolder<H>> Theme(block: () -> String) = Theme.get<H, T>(block)