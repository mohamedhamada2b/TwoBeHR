package com.twoB.hr.di

import com.twob.domain.authrepo.HomeRepo
import com.twob.domain.usecase.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {
    @Singleton
    @Provides
    fun provides_home_use_case(homeRepo : HomeRepo): HomeUseCase {
        return HomeUseCase(homeRepo)
    }
}