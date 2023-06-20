package com.twoB.hr.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.twoB.hr.R
import com.twoB.hr.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    lateinit var fragmentSplashBinding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        Handler().postDelayed({fragmentSplashBinding.logoImg.visibility = View.VISIBLE }
            , 500)
        Handler().postDelayed({
            val animZoomIn: Animation = AnimationUtils.loadAnimation(
                context?.applicationContext,
                R.anim.zoom_in
            )
            fragmentSplashBinding.logoImg.startAnimation(animZoomIn) }
            , 1000)
        Handler().postDelayed({fragmentSplashBinding.root.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)}
            , 2000)
        return fragmentSplashBinding.root
    }
}