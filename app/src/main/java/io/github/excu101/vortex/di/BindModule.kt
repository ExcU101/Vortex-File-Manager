package io.github.excu101.vortex.di

import dagger.Binds
import dagger.Module
import io.github.excu101.vortex.provider.FileOperationActionHandler
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.provider.storage.StorageOperationActionHandler
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.impl.StorageBookmarkProviderImpl
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl

@Module
interface BindModule {

    @Binds
    fun bindStorageProvider(
        impl: StorageProviderImpl,
    ): StorageProvider

    @Binds
    fun bindStorageBookmarkProvider(
        impl: StorageBookmarkProviderImpl,
    ): StorageBookmarkProvider

    @Binds
    fun bindFileOperationActionHandler(
        impl: StorageOperationActionHandler,
    ): FileOperationActionHandler<String?>

}