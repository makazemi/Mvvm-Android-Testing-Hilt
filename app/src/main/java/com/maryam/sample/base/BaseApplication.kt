package com.maryam.sample.base

import android.app.Application
import com.maryam.sample.di.AppComponent
import com.maryam.sample.di.DaggerAppComponent


open class BaseApplication: Application(){

    val appComponent: AppComponent by lazy {
        initAppComponent()
    }

    open fun initAppComponent():AppComponent{
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent()


    }

}