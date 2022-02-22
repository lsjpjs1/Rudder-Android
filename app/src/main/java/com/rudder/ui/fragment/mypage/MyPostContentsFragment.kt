package com.rudder.ui.fragment.mypage

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentCommunityContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.ui.fragment.community.CommunityDisplayFragmentDirections
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import kotlinx.android.synthetic.main.fragment_my_post_display.*

class MyPostContentsFragment : Fragment(), CustomOnclickListener {

    lateinit var viewModel: MyPostViewModel

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }
    val args by lazy {
        (parentFragment as MyPostDisplayFragment).args
    }
    lateinit var adapter : MainPostPreviewAdapter
    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }

    private val mainViewModel : MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (args.viewModelIndex == MyPostDisplayFragment.MY_COMMENT){
            viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyCommentViewModel::class.java)
        }else if (args.viewModelIndex == MyPostDisplayFragment.MY_POST){
            viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyPostViewModel::class.java)
        }
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityContentsBinding>(inflater,R.layout.fragment_community_contents,container,false)
        communityDisplay.mainVM = viewModel

        adapter = MainPostPreviewAdapter(this,lazyContext, viewModel,viewLifecycleOwner)
        adapter.submitList(viewModel.posts.value!!.toMutableList().map { it.copy() })
        communityDisplay.lifecycleOwner = this
        communityDisplay.postPreviewRV.also{
            it.layoutManager= LinearLayoutManager(lazyContext)
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
                    viewModel.refreshPosts()
                }

            }
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
                    viewModel.refreshPosts()
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
                    viewModel.refreshPosts()
                    if (parentActivity.showPostContentsFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })


        mainViewModel.selectedCommentGroupNum.observe(viewLifecycleOwner, Observer {
            if (it != -1) {
                (activity as MainActivity).showParentCommentInfo()
            } else {
                (activity as MainActivity).hideParentCommentInfo()
            }
        })


        viewModel.refreshPosts()

        return communityDisplay.root
    }




    override fun onClickView(view: View, position: Int) {
        viewModel.setSelectedPostPosition(position)
        var action = MyPostDisplayFragmentDirections.actionNavigationMyPostToNavigationShowPost(
            ShowPostDisplayFragment.MY_POST_VIEW_MODEL)
        if(args.viewModelIndex == MyPostDisplayFragment.MY_COMMENT){
            action = MyPostDisplayFragmentDirections.actionNavigationMyPostToNavigationShowPost(
                ShowPostDisplayFragment.MY_COMMENT_VIEW_MODEL)
        }

        view.findNavController().navigate(action)
        (activity as MainActivity).mainBottomNavigationDisappear()

        viewModel.addPostViewCount()
        viewModel.getComments()
    }


}