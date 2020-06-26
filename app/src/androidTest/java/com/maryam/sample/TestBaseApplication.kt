package com.maryam.sample

import com.maryam.sample.base.BaseApplication
import com.maryam.sample.di.AppComponent
import com.maryam.sample.di.DaggerTestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@OptIn(InternalCoroutinesApi::class)
class TestBaseApplication : BaseApplication(){

    override fun initAppComponent():AppComponent {
        return DaggerTestAppComponent.builder().application(this).build()
    }



}










