package io.github.excu101.vortex.di

import dagger.Module
import dagger.Provides
import io.github.excu101.vortex.utils.DispatcherProvider
import javax.inject.Singleton

@Module
object DispatcherModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProvider()

}