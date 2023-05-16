package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.excu101.vortex.provider.settings.Settings
import javax.inject.Singleton

@Module
object SettingsModule {

    @Provides
    @Singleton
    fun SettingsProvider(
        context: Context,
    ) = Settings(context)


}