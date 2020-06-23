package com.maryam.sample.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity(){

    @Inject
    lateinit var  viewModelFactory: ViewModelProvider.Factory

    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

}
