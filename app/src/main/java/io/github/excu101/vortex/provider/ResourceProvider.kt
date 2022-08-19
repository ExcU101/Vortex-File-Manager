package io.github.excu101.vortex.provider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return context.getDrawable(id)
    }

    inline operator fun <reified T> get(id: Int): T {
        return when (T::class) {
            Drawable::class -> getDrawable(id) as T
            String::class -> getString(id) as T
            else -> throw UnsupportedClassVersionError()
        }
    }

}