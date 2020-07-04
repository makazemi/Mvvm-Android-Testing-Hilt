package com.maryam.sample.ui.postList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maryam.sample.R
import com.maryam.sample.base.BaseApplication
import com.maryam.sample.base.BaseFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryam.sample.model.Post
import kotlinx.android.synthetic.main.fragment_list_post.*

class ListPostFragment : BaseFragment() {

    private val viewModel: PostListViewModel by viewModels {
        viewModelFactory
    }
    private lateinit var postAdapter: PostAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_post, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRcy()
        subscribeObserverPost()

    }

    private fun initRcy() {
        postAdapter = PostAdapter()
        postAdapter.setClickListenerRoot { itemClick(it) }
        rcy_post.apply {
            layoutManager = LinearLayoutManager(this@ListPostFragment.context)
            adapter = postAdapter
        }

    }

    private fun subscribeObserverPost() {
        viewModel.listPostApiOnly.observe(viewLifecycleOwner, Observer {
            it?.data?.peekContent()?.let {
                postAdapter.submitList(it)
            }
            onDataStateChange(it.loading, it.error)
        })
    }

    private fun itemClick(item: Post) {
        navigateToDetail(item)
    }

    private fun navigateToDetail(item: Post) {
        val action =
            ListPostFragmentDirections.actionListPostFragmentToDetailPostFragment(item)
        if (findNavController().currentDestination?.id == R.id.listPostFragment)
            this.findNavController().navigate(action)
    }

    override fun displayProgressBar(inProgress: Boolean) {
        if (inProgress)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcy_post.adapter = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }
}