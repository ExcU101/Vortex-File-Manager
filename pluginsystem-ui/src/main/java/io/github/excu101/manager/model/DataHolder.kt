package io.github.excu101.manager.model

import kotlin.reflect.KProperty

interface DataHolder<T> {
    val value: T
}

operator fun <T : Any> DataHolder<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value