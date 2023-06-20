package com.twoB.hr.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twob.domain.entity.Auth
import com.twob.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(var authUseCase: AuthUseCase):ViewModel() {
     lateinit var auth_single : Single<Auth>
     var mutableLiveData:MutableLiveData<Auth> = MutableLiveData<Auth>()
     var compositeDisposable = CompositeDisposable()
     fun login_user(id: String, password: String, deviceId: String) {
          auth_single = authUseCase.Login(id,password,deviceId)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
          compositeDisposable.add(auth_single.subscribe({ auth: Auth -> mutableLiveData.value = auth},{e:Throwable ->Log.e("error6",e.message.toString())}))
     }



}