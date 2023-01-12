package io.github.excu101.vortex.provider.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.settingsStore by preferencesDataStore("")

class Settings @Inject constructor(
    private val context: Context
) {
    private val store = context.settingsStore

    suspend fun <T> write(
        key: Preferences.Key<T>,
        value: T
    ) {
        store.edit {

        }
    }

    fun <T : Any?> read(
        // TODO: Replace with throw?
        onCatch: (Throwable) -> Unit = {},
        transform: (Preferences) -> T
    ): Flow<T> {
        return store.data.catch { exception ->
            if (exception is IOException) emptyPreferences()
            else onCatch(exception)
        }.map {
            transform(it)
        }
    }

    fun <T> read(
        onCatch: (Throwable) -> Unit = {},
        key: Preferences.Key<T>,
        default: T
    ): Flow<T> {
        return read(
            onCatch = onCatch,
            transform = { preferences ->
                preferences[key] ?: default
            }
        )
    }

}