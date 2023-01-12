package io.github.excu101.vortex.data.settings

import androidx.datastore.preferences.core.Preferences

interface SettingsItem<T> {
    val title: String
    val key: Preferences.Key<T>
}

typealias BooleanSettingsItem = SettingsItem<Boolean>
typealias IntSettingsItem = SettingsItem<Int>
typealias FloatSettingsItem = SettingsItem<Float>
typealias DoubleSettingsItem = SettingsItem<Double>

operator fun <T> SettingsItem<T>.component1() = title
operator fun <T> SettingsItem<T>.component2() = key