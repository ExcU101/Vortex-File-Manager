package io.github.excu101.vortex.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.main.MainAction
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun resources(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }

    @Singleton
    @Provides
    fun actions(resources: ResourceProvider): MainAction {
        return MainAction(resources)
    }

}