package com.rudder.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel

class CommunityDisplayFragment(val fm: FragmentManager): Fragment(),CustomOnclickListener {

    private val viewModel :MainViewModel by activityViewModels()

    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityDisplayBinding>(inflater,R.layout.fragment_community_display,container,false)
        val adapter = PostPreviewAdapter(viewModel.posts.value!!,this,lazyContext)
        communityDisplay.postPreviewRV.also{
            it.layoutManager=LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        viewModel.scrollTouchBottom()
                    }
                }
            })
        }

        viewModel.commentCountChange.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(viewModel.selectedPostPosition.value!!)
        })
        viewModel.isLikePost.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(viewModel.selectedPostPosition.value!!)
        })
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.updatePosts(it)
        })

        return communityDisplay.root
    }

    override fun onClick(view: View, position: Int) {
        (activity as MainActivity).changeSelectedPostPosition(position)
        (activity as MainActivity).showPost()
        (activity as MainActivity).showAddComment()
        if(!viewModel.isAlreadyReadPost()){
            viewModel.addPostViewCount()
        }
        viewModel.getComments()
    }



}