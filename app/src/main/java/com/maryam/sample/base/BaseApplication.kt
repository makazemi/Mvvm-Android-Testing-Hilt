package com.maryam.sample.base

import android.app.Application
import com.maryam.sample.di.AppComponent
import com.maryam.sample.di.DaggerAppComponent


class BaseApplication: Application(){


    lateinit var appComponent: AppComponent


    private fun initAppComponent(){
        appComponent= DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent()

    }

}