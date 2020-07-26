package com.maryam.sample.util

import android.content.Context
import android.net.ConnectivityManager

class SessionManager
constructor(
 val application: Context
) {
    fun isConnectedToTheInternet(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
        }
        return false
    }


}