package com.twoB.hr.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twob.domain.entity.FingerPrint
import com.twob.domain.entity.UserBranchesItem
import com.twob.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val branchesUseCase: HomeUseCase):ViewModel() {
     var branches_live_data = MutableLiveData<List<UserBranchesItem>>()
     lateinit var branches_observable:Single<List<UserBranchesItem>>
     lateinit var finger_print_observable:Single<FingerPrint>
     var finger_print_live_data = MutableLiveData<FingerPrint>()
     var branches_title_live_data = MutableLiveData<List<String>>()
     val compositeDisposable = CompositeDisposable()
     var branchestitlelist:ArrayList<String> = ArrayList()
     fun get_branches(national_id: String) {
          branches_observable = branchesUseCase.get_user_branches(national_id)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
          compositeDisposable.add(branches_observable.subscribe({user_branches:List<UserBranchesItem> -> setData(user_branches)},{e:Throwable -> e.message.toString()}))
          }

     private fun setData(userBranches: List<UserBranchesItem>) {
          for (UserBranchesItem in userBranches){
               branchestitlelist.add(UserBranchesItem.locName)
          }
          branches_title_live_data.value = branchestitlelist
          branches_live_data.value = userBranches
     }

     private fun setfingerprintData(fingerprint: FingerPrint) {
          finger_print_live_data.value = fingerprint
     }

     fun add_finger_print(empId: Int?, locationName: String?, locationName1: String?, distance: Double, latitude: Double, longitude: Double) {
          finger_print_observable = branchesUseCase.add_finger_print(empId.toString(),
               locationName!!, locationName1!!, distance.toString(),latitude.toString(),longitude.toString())
               .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
          compositeDisposable.add(finger_print_observable.subscribe({fingerprint:FingerPrint -> setfingerprintData(fingerprint)},{e:Throwable-> e.message.toString()}))

     }

     override fun onCleared() {
          super.onCleared()
          compositeDisposable.clear()
     }
}

