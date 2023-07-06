package io.github.excu101.vortex.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.excu101.vortex.provider.storage.AndroidStorageHelper
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.ui.screen.storage.StorageStateController
import javax.inject.Singleton

@Component(modules = [BindModule::class, StorageModule::class])
@Singleton
interface AppComponent {

    val storage: StorageProvider

    val androidStorageHelper: AndroidStorageHelper

    val storageStateController: StorageStateController

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindContext(context: Context): Builder

        fun build(): AppComponent
    }

}