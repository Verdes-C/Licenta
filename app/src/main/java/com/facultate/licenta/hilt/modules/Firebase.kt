package com.facultate.licenta.hilt.modules

import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Firebase {

    @Provides
    @Singleton
    fun providesFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductRepository(
        repository: Repository
    ): FirebaseRepository
}