package com.rudder.ui.fragment.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentCommunityContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel

class CommunityContentsFragment: Fragment(),CustomOnclickListener {


    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }
    private val viewModel : MainViewModel by activityViewModels()


    lateinit var adapter : MainPostPreviewAdapter

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityContentsBinding>(inflater,R.layout.fragment_community_contents,container,false)
        communityDisplay.mainVM = viewModel

        adapter = MainPostPreviewAdapter(this,lazyContext, viewModel,viewLifecycleOwner)
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
                        viewModel.scrollTouchBottomCommunityPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {
                        //viewModel.scrollTouchTop()
                    }
                }
            })
        }


        communityDisplay.communityContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        communityDisplay.communityContentsSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.scrollTouchTopCommunityPost()
            }

        })


        communityDisplay.communityContentsSwipeRefreshLayout


        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.posts.value!!.toMutableList().map { it.copy() })
            communityDisplay.communityContentsSwipeRefreshLayout.isRefreshing = false

        })

        viewModel.isAddPostSuccess.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it){
                    viewModel.clearPosts()
                    viewModel.getPosts()
                }

            }
        })

        viewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->


            //Log.d("test_contents", "${it.getContentIfNotHandled()}")


            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                    (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
            }

            //Log.d("test_contents2", "${it.getContentIfNotHandled()}")

//            it?.let {
//                if(it)
//                (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
//            }


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
                    if (parentActivity.showPostContentsFragment.isVisible){
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
                    if (parentActivity.showPostContentsFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })

//        viewModel.isScrollBottomTouch.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled().let {
//                it?.let{
//                    if (it){
//                        parentActivity.showProgressBar()
//                    } else {
//                        parentActivity.hideProgressBar()
//                    }
//                }
//            }
//        })

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

    override fun onClickView(view: View, position: Int) {
        Log.d("activity-main-vm",viewModel.toString())
        viewModel.setSelectedPostPosition(position)
        //(activity as MainActivity). showPost(viewModel, ShowPostContentsFragment(viewModel))

        val action = CommunityDisplayFragmentDirections.actionNavigationCommunityToNavigationShowPost(
            ShowPostDisplayFragment.MAIN_VIEW_MODEL)
        view.findNavController().navigate(action)
        (activity as MainActivity).mainBottomNavigationDisappear()


        //(activity as MainActivity).showAddComment(AddCommentFragment(viewModel))
//        if(!viewModel.isAlreadyReadPost()){
//            viewModel.addPostViewCount()
//        }
        viewModel.addPostViewCount()
        viewModel.getComments()
    }




}