package com.twob.data.repo

import com.twob.data.remote.Api
import com.twob.domain.authrepo.HomeRepo
import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranches
import com.twob.domain.entity.UserBranchesItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Named

class HomeRepoImp(private val api: Api) : HomeRepo {
    lateinit var single_branches : Single<List<UserBranchesItem>>
    lateinit var single_finger_print :Single<FingerPrint>

    override fun get_user_branches(national_id:String): Single<List<UserBranchesItem>> {
        single_branches = api.user_Branches(national_id)
        return single_branches
    }

    override fun add_finger_print(
        empid: String,
        locationfingerprint: String,
        locationchoosing: String,
        distance: String,
        latfp: String,
        lonfp: String
    ): Single<FingerPrint> {
        single_finger_print = api.add_finger_print(empid,locationfingerprint,locationchoosing,distance,latfp,lonfp)
        return single_finger_print
    }

}