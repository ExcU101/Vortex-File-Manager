package io.github.excu101.vortex.di

import dagger.Binds
import dagger.Module
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl

@Module
interface BindModule {

    @Binds
    fun bindStorageProvider(
        impl: StorageProviderImpl,
    ): StorageProvider

}