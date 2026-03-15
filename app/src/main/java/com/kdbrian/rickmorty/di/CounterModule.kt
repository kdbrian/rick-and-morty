package com.kdbrian.rickmorty.di

import com.kdbrian.rickmorty.data.local.impl.AppCounterRepoImpl
import com.kdbrian.rickmorty.domain.repo.AppCounterRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CounterModule {


    @Binds
    @Singleton
    abstract fun providesAppCounterRepo(
        appCounterRepoImpl: AppCounterRepoImpl
    ): AppCounterRepo

}