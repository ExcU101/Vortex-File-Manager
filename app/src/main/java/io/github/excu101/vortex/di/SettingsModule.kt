package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortex.provider.settings.Settings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun SettingsProvider(
        @ApplicationContext
        context: Context
    ) = Settings(context)


}