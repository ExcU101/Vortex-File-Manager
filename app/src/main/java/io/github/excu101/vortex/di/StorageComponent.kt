package io.github.excu101.vortex.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.ui.screen.storage.StoragePageController
import io.github.excu101.vortex.ui.screen.storage.StorageStateController
import javax.inject.Singleton

@Component(modules = [BindModule::class, StorageModule::class])
@Singleton
interface StorageComponent {

    val provider: StorageProvider

    val controller: StorageStateController

    fun inject(controller: StoragePageController)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindContext(context: Context): Builder

        fun build(): StorageComponent
    }

}