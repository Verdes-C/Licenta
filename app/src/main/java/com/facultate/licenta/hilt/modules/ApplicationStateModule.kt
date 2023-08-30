package com.facultate.licenta.hilt.modules

import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationStateModule {

    @Provides
    @Singleton
    fun providesApplicationState(): Store<ApplicationState> {
        return Store(ApplicationState())
    }

}