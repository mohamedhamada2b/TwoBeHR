package com.twob.data.remote

import com.twob.domain.entity.Auth
import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranches
import com.twob.domain.entity.UserBranchesItem
import io.reactivex.rxjava3.core.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @POST("url/api/UsersPersonals/PostUsersPersonal")
    fun login_user(@Query("UserName")UserName:String,@Query("PassWord")PassWord:String,@Query("mac")mac:String): Single<Auth>

    @POST("url2/api/UsersPersonals/PostUsersPersonal")
    fun user_Branches(@Query("nationalid")nationalid:String):Single<List<UserBranchesItem>>

    @POST("url3/api/UsersPersonals")
    fun add_finger_print(@Query("empid")empid:String,@Query("locationfingerprint")locationfingerprint:String,@Query("locationchoosing")locationchoosing:String,@Query("distance")distance:String,@Query("latfp")latfp:String,@Query("lonfp")lonfp:String):Single<FingerPrint>


}