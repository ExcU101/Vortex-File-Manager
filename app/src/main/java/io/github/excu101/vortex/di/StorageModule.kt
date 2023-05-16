package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import javax.inject.Singleton

@Module
object StorageModule {
    @Provides
    @Singleton
    fun provideStorageProviderImpl(context: Context): StorageProviderImpl {
        return StorageProviderImpl(context)
    }
}