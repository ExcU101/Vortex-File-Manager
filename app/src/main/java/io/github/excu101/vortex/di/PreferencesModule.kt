package io.github.excu101.vortex.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortex.provider.settings.Settings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun SettingsPreferences(@ApplicationContext context: Context): Settings {
        return Settings(store = context.SettingsStore)
    }

}

private val Context.SettingsStore by preferencesDataStore(name = "settings")