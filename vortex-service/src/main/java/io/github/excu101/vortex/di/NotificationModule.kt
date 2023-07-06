package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.excu101.vortex.service.notification.VortexNotificationManager
import javax.inject.Singleton

@Module
object NotificationModule {

    @Provides
    @Singleton
    fun createVortexNotificationManager(
        context: Context,
    ): VortexNotificationManager = VortexNotificationManager(context)

}