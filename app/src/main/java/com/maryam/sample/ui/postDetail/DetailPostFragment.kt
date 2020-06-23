package com.maryam.sample.ui.postDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maryam.sample.R
import com.maryam.sample.base.BaseApplication
import com.maryam.sample.base.BaseFragment

class DetailPostFragment : BaseFragment() {


    override fun displayProgressBar(inProgress: Boolean) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as BaseApplication).appComponent.inject(this)
    }
}