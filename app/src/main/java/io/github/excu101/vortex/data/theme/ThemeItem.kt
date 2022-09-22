package io.github.excu101.vortex.data.theme

import android.os.Parcelable
import io.github.excu101.vortex.ui.component.list.adapter.Item

abstract class ThemeItem<T> : Item<T>, Parcelable {
    abstract val key: String

    override val id: Long
        get() = key.hashCode().toLong()
}