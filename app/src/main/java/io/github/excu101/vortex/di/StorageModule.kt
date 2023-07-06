package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.excu101.vortex.provider.storage.AndroidStorageHelper
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.utils.DispatcherProvider
import javax.inject.Singleton

@Module(includes = [DispatcherModule::class])
object StorageModule {
    @Provides
    @Singleton
    fun provideStorageProviderImpl(
        context: Context,
        dispatchers: DispatcherProvider,
    ): StorageProviderImpl = StorageProviderImpl(context, dispatchers)

    @Provides
    @Singleton
    fun provideAndroidStorageHelper(
        context: Context,
    ): AndroidStorageHelper = AndroidStorageHelper(context)
}