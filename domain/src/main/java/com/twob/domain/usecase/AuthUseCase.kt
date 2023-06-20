package com.twob.domain.usecase

import com.twob.domain.authrepo.AuthRepo

class AuthUseCase(private val authRepo: AuthRepo) {
    fun Login(UserName:String,PassWord:String,mac:String) = authRepo.Login(UserName,PassWord,mac)
}