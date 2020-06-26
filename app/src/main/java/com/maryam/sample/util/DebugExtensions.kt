package com.maryam.sample.util

import android.util.Log
import com.maryam.sample.util.Constant.DEBUG
import com.maryam.sample.util.Constant.TAG


fun printLogD(className: String?, message: String ) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}