package com.twoB.hr.di

import com.twob.data.remote.Api
import com.twob.data.repo.AuthRepoImp
import com.twob.domain.authrepo.AuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepoModule {
    @Singleton
    @Provides
    fun provides_auth_repo(api: Api):AuthRepo{
        return AuthRepoImp(api)
    }
}