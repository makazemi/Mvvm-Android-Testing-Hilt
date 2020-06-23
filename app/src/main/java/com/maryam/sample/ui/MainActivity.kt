package com.maryam.sample.ui

import android.os.Bundle
import com.maryam.sample.R
import com.maryam.sample.base.BaseActivity
import com.maryam.sample.base.BaseApplication

class MainActivity : BaseActivity() {


    override fun inject() {
        (application as BaseApplication).appComponent
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}