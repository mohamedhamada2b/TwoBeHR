package com.twob.domain.authrepo

import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranchesItem
import io.reactivex.rxjava3.core.Single

interface HomeRepo {
    fun get_user_branches(national_id:String):Single<List<UserBranchesItem>>
    fun add_finger_print(empid:String,locationfingerprint:String,locationchoosing:String,distance:String,latfp:String,lonfp:String):Single<FingerPrint>


}