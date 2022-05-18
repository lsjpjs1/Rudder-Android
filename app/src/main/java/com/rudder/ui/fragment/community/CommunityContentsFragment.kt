package com.rudder.ui.fragment.community

import android.os.Bundle
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
import com.rudder.R
import com.rudder.databinding.FragmentCommunityContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_community_contents.view.*

open class CommunityContentsFragment: Fragment(), CustomOnclickListener {

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }
    private val mainViewModel : MainViewModel by activityViewModels()
    lateinit var adapter : MainPostPreviewAdapter
    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityContentsBinding>(inflater,R.layout.fragment_community_contents,container,false)
        adapter = MainPostPreviewAdapter(this,lazyContext, mainViewModel,viewLifecycleOwner)
        adapter.submitList(mainViewModel.posts.value!!.toMutableList().map { it.copy() })
        communityDisplay.mainVM = mainViewModel
        communityDisplay.lifecycleOwner = this
        communityDisplay.postPreviewRV.also{
            it.layoutManager=LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        mainViewModel.scrollTouchBottomCommunityPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {

                    }
                }
            })
        }

        return communityDisplay.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.communityContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        view.communityContentsSwipeRefreshLayout.setOnRefreshListener {
            mainViewModel.scrollTouchTopCommunityPost()
        }


        mainViewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(mainViewModel.posts.value!!.toMutableList().map { it.copy() })
            view.communityContentsSwipeRefreshLayout.isRefreshing = false
        })

        mainViewModel.isAddPostSuccess.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if(it){
                    mainViewModel.clearPosts()
                    //mainViewModel.getPosts()
                    mainViewModel.getPostsFun()

                }
            }
        })

        mainViewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                    (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(mainViewModel))
            }
        })

        mainViewModel.isPostDelete.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(
                        context,
                        "Delete Post Complete!",
                        Toast.LENGTH_LONG
                    ).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    mainViewModel.clearPosts()
                    //mainViewModel.getPosts()
                    mainViewModel.getPostsFun()

                    if (parentActivity.showPostContentsFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })

        mainViewModel.isBlockUser.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(
                        context,
                        "Block User Complete!",
                        Toast.LENGTH_LONG
                    ).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    mainViewModel.clearPosts()
                    //mainViewModel.getPosts()
                    mainViewModel.getPostsFun()

                    if (parentActivity.showPostContentsFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })


    }

    override fun onClickView(view: View, position: Int) {
        mainViewModel.setSelectedPostPosition(position)

        val action = CommunityDisplayFragmentDirections.actionNavigationCommunityToNavigationShowPost(
            ShowPostDisplayFragment.MAIN_VIEW_MODEL)
        view.findNavController().navigate(action)
        (activity as MainActivity).mainBottomNavigationDisappear()

        //mainViewModel.addPostViewCount()
    }




}