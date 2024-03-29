package io.github.excu101.vortex.theme.model

import kotlin.reflect.KProperty

interface DataHolder<T> {
    val value: T
}

operator fun <T : Any> DataHolder<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value