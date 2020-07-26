package com.maryam.sample.base


import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
open class BaseApplication: MultiDexApplication()