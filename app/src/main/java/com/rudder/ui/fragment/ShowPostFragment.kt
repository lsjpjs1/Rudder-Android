package com.rudder.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentShowPostBinding
import com.rudder.ui.adapter.PostCommentsAdapter
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_show_post.view.*

class ShowPostFragment: Fragment() {
    private val viewModel = MainViewModel
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding= DataBindingUtil.inflate<FragmentShowPostBinding>(inflater,R.layout.fragment_show_post,container,false)

        val adapter = PostCommentsAdapter(viewModel.comments.value!!,lazyContext)
        fragmentBinding.commentDisplayRV.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = adapter
        }
        fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            adapter.updateComments(it)
        })
        viewModel.selectedPostPosition.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        })
        childFragmentManager.beginTransaction()
                .add(R.id.showPostHeader,ShowPostHeaderFragment())
                .commit()



        return fragmentBinding.root
    }
}