package io.github.excu101.vortex.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.vortex.service.notification.VortexNotificationManager
import javax.inject.Singleton

@Component(modules = [NotificationModule::class, FileSystemModule::class])
@Singleton
interface ServiceComponent {

    val notifier: VortexNotificationManager

    val fileSystem: FileSystem

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder
        fun build(): ServiceComponent
    }

}