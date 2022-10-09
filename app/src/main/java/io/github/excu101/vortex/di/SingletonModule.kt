package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.provider.storage.StorageProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun storageProvider(@ApplicationContext context: Context): StorageProvider {
        return StorageProvider(context)
    }

    @Singleton
    @Provides
    fun resourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }

    @Singleton
    @Provides
    fun storageActionProvider(resources: ResourceProvider): StorageActionProvider {
        return StorageActionProvider(resources)
    }

}