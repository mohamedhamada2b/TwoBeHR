package com.twob.data.repo

import com.twob.data.remote.Api
import com.twob.domain.authrepo.AuthRepo
import com.twob.domain.entity.Auth
import io.reactivex.rxjava3.core.Single

class AuthRepoImp(private var api: Api):AuthRepo {
    lateinit var login_observable :Single<Auth>
    override fun Login(UserName: String, PassWord: String, mac: String): Single<Auth> {
        login_observable = api.login_user(UserName,PassWord,mac)
        return login_observable
    }

}