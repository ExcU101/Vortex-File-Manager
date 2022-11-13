package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.provider.storage.StorageOperationActionHandler
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun StorageProvider(@ApplicationContext context: Context): StorageProviderImpl {
        return StorageProviderImpl(context)
    }

    @Provides
    @Singleton
    fun StorageOperationActionHandlerProvider(): StorageOperationActionHandler {
        return StorageOperationActionHandler()
    }

    @Provides
    @Singleton
    fun StorageActions(resources: ResourceProvider): StorageActionProvider {
        return StorageActionProvider(resources)
    }

    @Provides
    @Singleton
    fun StorageBookmarkProvider(): StorageBookmarkProvider {
        return StorageBookmarkProvider
    }

}