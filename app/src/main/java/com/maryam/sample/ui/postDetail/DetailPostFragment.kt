package com.maryam.sample.ui.postDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.maryam.sample.R
import com.maryam.sample.base.BaseApplication
import com.maryam.sample.base.BaseFragment
import com.maryam.sample.model.Post
import kotlinx.android.synthetic.main.fragment_detail_post.*
import kotlinx.android.synthetic.main.post_item_rcy.txt_id
import kotlinx.android.synthetic.main.post_item_rcy.txt_title

class DetailPostFragment : BaseFragment() {

    private val args: DetailPostFragmentArgs by navArgs()

    override fun displayProgressBar(inProgress: Boolean) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val post:Post?=args.postArg
        post?.let {
            initView(it)
        }

    }

    private fun initView(item:Post){
        txt_id.text=item.id.toString()
        txt_title.text=item.title
        txt_body.text=item.body
        txt_user_id.text=item.userId.toString()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }
}