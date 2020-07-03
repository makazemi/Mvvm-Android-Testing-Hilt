package com.maryam.sample.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maryam.sample.repository.Event
import com.maryam.sample.repository.Loading
import com.maryam.sample.util.ErrorBody
import javax.inject.Inject

abstract class BaseFragment:Fragment() {

    val TAG="BaseFragment"

    @Inject
    lateinit var  viewModelFactory: ViewModelProvider.Factory

    fun onDataStateChange(loading: Loading, error: Event<ErrorBody>?){
        error?.let {
            it.getContentIfNotHandled()?.let {
                Log.d(TAG,"error=${it.message}")
                Toast.makeText(this.context,it.message, Toast.LENGTH_SHORT).show()
            }
        }
        Log.d(TAG,"isloading=${loading.isLoading}")
        displayProgressBar(loading.isLoading)

    }
    abstract fun displayProgressBar(inProgress:Boolean)
}