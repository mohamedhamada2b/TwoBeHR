package com.twob.domain.usecase

import com.twob.domain.authrepo.HomeRepo

class HomeUseCase(private val homeRepo : HomeRepo) {
    fun get_user_branches(national_id:String) = homeRepo.get_user_branches(national_id)
    fun add_finger_print(empid:String,locationfingerprint:String,locationchoosing:String,distance:String,latfp:String,lonfp:String)= homeRepo.add_finger_print(empid,locationfingerprint,locationchoosing,distance,latfp,lonfp)
}