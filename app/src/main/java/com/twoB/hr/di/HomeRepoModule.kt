package com.twoB.hr.di

import com.twob.data.remote.Api
import com.twob.data.repo.HomeRepoImp
import com.twob.domain.authrepo.HomeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object HomeRepoModule {

    @Singleton
    @Provides
    fun provides_home_repo(api:Api):HomeRepo{
        return HomeRepoImp(api)
    }
}