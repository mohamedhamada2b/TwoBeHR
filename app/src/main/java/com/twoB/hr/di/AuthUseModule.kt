package com.twoB.hr.di

import com.twob.domain.authrepo.AuthRepo
import com.twob.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseModule {
    @Singleton
    @Provides
    fun provides_auth_use_case(authRepo: AuthRepo):AuthUseCase{
        return AuthUseCase(authRepo)
    }
}