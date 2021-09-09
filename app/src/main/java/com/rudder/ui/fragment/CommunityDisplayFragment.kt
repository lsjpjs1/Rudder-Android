package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val adapter = PostPreviewAdapter(viewModel.posts.value!!,this,lazyContext, viewModel)
        communityDisplay.postPreviewRV.also{
            it.layoutManager=LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        viewModel.scrollTouchBottom()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {
                        viewModel.scrollTouchTop()
                    }
                }
            })
        }

        viewModel.commentCountChange.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(viewModel.selectedPostPosition.value!!)
        })
        viewModel.postInnerValueChangeSwitch.observe(viewLifecycleOwner, Observer {

            it?.let {
                viewModel.selectedPostPosition.value?.let {
                    adapter.notifyItemChanged(viewModel.selectedPostPosition.value!!)
                }

            }

        })
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            Log.d("posts.observe","posts.observe")
            adapter.updatePosts(it)
        })

        viewModel.isAddPostSuccess.observe(viewLifecycleOwner, Observer {
            viewModel.clearPosts()
            viewModel.getPosts()
        })


//        viewModel.isEditPostSuccess.observe(viewLifecycleOwner, Observer {
//            viewModel.clearPosts()
//            viewModel.getPosts()
//
//            GlobalScope.launch {
//                viewModel.getPosts()
//                (activity as MainActivity).showPost()
//            }
//
//            //(activity as MainActivity).showPost()
//        })



//        viewModel.asd.observe(viewLifecycleOwner, Observer {
//            (activity as MainActivity).showPost()
//        })



        return communityDisplay.root
    }

    override fun onClick(view: View, position: Int) {
        (activity as MainActivity).changeSelectedPostPosition(position)
        (activity as MainActivity).showPost()
        (activity as MainActivity).showAddComment()
//        if(!viewModel.isAlreadyReadPost()){
//            viewModel.addPostViewCount()
//        }
        viewModel.addPostViewCount()
        viewModel.getComments()
    }



}