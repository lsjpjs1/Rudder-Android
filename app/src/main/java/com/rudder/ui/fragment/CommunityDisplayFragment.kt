package com.rudder.ui.fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.PreviewPost
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import kotlinx.coroutines.launch

class CommunityDisplayFragment(val viewModel: MainViewModel): Fragment(),CustomOnclickListener {


    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityDisplayBinding>(inflater,R.layout.fragment_community_display,container,false)
        val adapter = MainPostPreviewAdapter(this,lazyContext, viewModel)
        adapter.submitList(viewModel.posts.value!!.toMutableList().map { it.copy() })
        communityDisplay.lifecycleOwner = this
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


        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.posts.value!!.toMutableList().map { it.copy() })
        })

        viewModel.isAddPostSuccess.observe(viewLifecycleOwner, Observer {
            viewModel.clearPosts()
            viewModel.getPosts()
        })

        viewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                    (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
            }
        })

        viewModel.isPostDelete.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(
                        context,
                        "Delete Post Complete!",
                        Toast.LENGTH_LONG
                    ).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    viewModel.clearPosts()
                    viewModel.getPosts()
                    if (parentActivity.showPostFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })

        viewModel.isBlockUser.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(
                        context,
                        "Block User Complete!",
                        Toast.LENGTH_LONG
                    ).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    viewModel.clearPosts()
                    viewModel.getPosts()
                    if (parentActivity.showPostFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })

        viewModel.isScrollBottomTouch.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                it?.let{
                    if (it){
                        parentActivity.showProgressBar()
                    } else {
                        parentActivity.hideProgressBar()
                    }
                }
            }
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





        return communityDisplay.root
    }

    override fun onClick(view: View, position: Int) {
        (activity as MainActivity).changeSelectedPostPosition(position)
        (activity as MainActivity).showPost(viewModel, ShowPostFragment(viewModel))
        (activity as MainActivity).showAddComment(AddCommentFragment(viewModel))
//        if(!viewModel.isAlreadyReadPost()){
//            viewModel.addPostViewCount()
//        }
        viewModel.addPostViewCount()
        viewModel.getComments()
    }



}