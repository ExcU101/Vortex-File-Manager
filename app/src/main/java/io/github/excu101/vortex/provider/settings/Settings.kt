package io.github.excu101.vortex.provider.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class Settings @Inject constructor(private val store: DataStore<Preferences>) {

    fun <T> read(provide: (Preferences) -> T): Flow<T> {
        return store.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw  exception
        }.map { preferences -> provide(preferences) }
    }

    suspend fun <T> write(key: Preferences.Key<T>, value: T) {
        store.edit { preferences ->
            preferences[key] = value
        }
    }

}