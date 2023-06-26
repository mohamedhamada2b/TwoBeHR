package com.twoB.hr.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.data.constants.Utilities
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.twoB.hr.R
import com.twoB.hr.databinding.BottomSheetErrorLocBinding
import com.twoB.hr.databinding.FragmentLoginBinding
import com.twoB.hr.viewmodel.AuthViewModel
import com.twob.domain.entity.Auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var fragmentLoginBinding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    lateinit var id:String
    lateinit var password:String
    lateinit var device_id:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentLoginBinding= FragmentLoginBinding.inflate(inflater, container, false)
        authViewModel.mutableLiveData.observe(viewLifecycleOwner){
            setData(it)
        }
        device_id = getSystemDetail()
        fragmentLoginBinding.btnLogin.setOnClickListener{
            validation()
        }
        fragmentLoginBinding.etUserId.doOnTextChanged { text, start, before, count ->
            if (text!!.length!=14){
                fragmentLoginBinding.textUserLy.isErrorEnabled = true
                fragmentLoginBinding.textUserLy.isHintEnabled = false
                fragmentLoginBinding.textUserLy.error = "Please enter a valid ID"
            }else if(text.length == 14){
                fragmentLoginBinding.textUserLy.error = null
            }
        }
        fragmentLoginBinding.etUserPassword.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()){
                fragmentLoginBinding.textUserPasswordLy.isErrorEnabled = true
                fragmentLoginBinding.textUserPasswordLy.isHintEnabled = false
                fragmentLoginBinding.textUserPasswordLy.error = "Please enter a valid Password"
            }else {
                fragmentLoginBinding.textUserPasswordLy.error = null
            }
        }
        id = fragmentLoginBinding.etUserId.text.toString()
        password = fragmentLoginBinding.etUserPassword.text.toString()

        return fragmentLoginBinding.root
    }

    private fun setData(it: Auth?) {
        if (it?.contains(1)!!){
            val bundle = Bundle()
            bundle.putString("national_id", id)
            fragmentLoginBinding.root.findNavController().navigate(R.id.action_loginFragment_to_homeFragment,bundle)
        }else if(it.contains(0) || it.contains(2)){
            createBottomSheet(it)
        }
    }

    private fun createBottomSheet(it: Auth?) {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        if (it?.contains(0)!!){
            binding.txtDesc.text = "Invalid User ID or Password"
        }else{
            binding.txtDesc.text = "Invalid Device"
        }
        bottomSheetDialog.show()
    }

    private fun createNetworkBottomSheet() {
        val bottomSheetDialog =  BottomSheetDialog(context!!)
        val inflater = LayoutInflater.from(requireContext())
        val binding = BottomSheetErrorLocBinding.inflate(inflater)
        bottomSheetDialog.setContentView(binding.root)
        binding.txtDesc.text = "Something went wrong,\n" +
                "Please try again!!!!"
        binding.locationImg.setImageResource(R.drawable.wrong)
        bottomSheetDialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun validation() {
        id = fragmentLoginBinding.etUserId.text.toString()
        password = fragmentLoginBinding.etUserPassword.text.toString()
        Log.e("device_id",device_id)
        if (!TextUtils.isEmpty(password)&& id.length== 14) {
            if (Utilities.isNetworkAvailable(context!!)) {
                authViewModel.login_user(id, password, id)
                fragmentLoginBinding.btnLogin.setBackgroundDrawable(resources.getDrawable(R.drawable.active_btn_bg))
            }else {
                createNetworkBottomSheet()
            }
            }else{
                if (id.length != 14){
                    fragmentLoginBinding.textUserLy.error = "Please enter a valid ID"
                    fragmentLoginBinding.textUserLy.isHintEnabled = false
                }else{
                    fragmentLoginBinding.textUserLy.error = null
                }
                if (TextUtils.isEmpty(password)){
                    fragmentLoginBinding.textUserPasswordLy.error = "Please enter a valid Password"
                    fragmentLoginBinding.textUserPasswordLy.isHintEnabled = false
                }else{
                    fragmentLoginBinding.textUserPasswordLy.error = null
                }
            }
        }

    @SuppressLint("HardwareIds")
    private fun getSystemDetail():String {
        return Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        //val deviceId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun onResume() {
        super.onResume()
        if (!TextUtils.isEmpty( fragmentLoginBinding.etUserPassword.text.toString())&& fragmentLoginBinding.etUserId.text.toString().length== 14){
            fragmentLoginBinding.btnLogin.setBackgroundDrawable(resources.getDrawable(R.drawable.active_btn_bg))
        }else{
            fragmentLoginBinding.btnLogin.setBackgroundDrawable(resources.getDrawable(R.drawable.inactive_btn_bg))
        }
    }
}