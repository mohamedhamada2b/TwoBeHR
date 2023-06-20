package com.twoB.hr.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication :Application() {
    /*lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()

    }
    fun getAppComponent(): AppComponent {
        return component
    }*/
}