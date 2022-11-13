package io.github.excu101.vortex.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.excu101.vortex.provider.FileOperationActionHandler
import io.github.excu101.vortex.provider.storage.StorageOperationActionHandler
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class AbsStorageModule {

    @Binds
    abstract fun bindStorageProvider(
        impl: StorageProviderImpl,
    ): StorageProvider

    @Binds
    abstract fun bindFileOperationActionHandler(
        impl: StorageOperationActionHandler,
    ): FileOperationActionHandler

}