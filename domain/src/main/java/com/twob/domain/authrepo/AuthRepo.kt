package com.twob.domain.authrepo

import com.twob.domain.entity.Auth
import io.reactivex.rxjava3.core.Single

interface AuthRepo {
    fun Login(UserName:String,PassWord:String,mac:String):Single<Auth>
}