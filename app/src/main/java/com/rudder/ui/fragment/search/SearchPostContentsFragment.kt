package com.rudder.ui.fragment.search

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SearchViewModel

class SearchPostContentsFragment  : Fragment(),CustomOnclickListener {

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy{
        activity as MainActivity
    }

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }
    private val mainViewModel : MainViewModel by activityViewModels()
    private val searchViewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchDisplayBinding = DataBindingUtil.inflate<FragmentSearchPostContentsBinding>(inflater,
            R.layout.fragment_search_post_contents,container,false)
        searchDisplayBinding.lifecycleOwner = this

        val adapter = MainPostPreviewAdapter(this,lazyContext, searchViewModel,viewLifecycleOwner)
        adapter.submitList(searchViewModel.posts.value!!.toMutableList().map { it.copy() })
        searchDisplayBinding.searchPostPreviewRV.also{
            it.layoutManager= LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        searchViewModel.scrollTouchBottomCommunityPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {
                    }
                }
            })
        }

        searchDisplayBinding.searchPostContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        searchDisplayBinding.searchPostContentsSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                searchViewModel.searchWord.value?.let {
                    searchViewModel.scrollTouchTopCommunityPost()
                } ?: kotlin.run {
                    searchDisplayBinding.searchPostContentsSwipeRefreshLayout.isRefreshing = false
                }

            }

        })

        searchViewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                    (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(searchViewModel))
            }
        })


        searchViewModel.posts.observe(viewLifecycleOwner, Observer { items ->
            adapter.submitList(items.toMutableList().map { it.copy() })
            searchDisplayBinding.searchPostContentsSwipeRefreshLayout.isRefreshing = false

        })


        searchViewModel.isPostDelete.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(context, "Delete Post Complete!", Toast.LENGTH_LONG).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    searchViewModel.clearPosts()
                    searchViewModel.searchPost(false)
                    if (parentActivity.showPostContentsFragment.isVisible){
                        parentActivity.onBackPressed()
                    }
                }
            }
        })


        searchViewModel.isBlockUser.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(
                        context,
                        "Block User Complete!",
                        Toast.LENGTH_LONG
                    ).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    searchViewModel.clearPosts()
                    searchViewModel.searchPost(false)
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


        searchViewModel.isSearchWordValid.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(context, "Please enter at least two letters.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return searchDisplayBinding.root
    }

    override fun onClickView(view: View, position: Int) {
        searchViewModel.setSelectedPostPosition(position)
        val action = SearchPostDisplayFragmentDirections.actionNavigationSearchToNavigationShowPost(ShowPostDisplayFragment.SEARCH_VIEW_MODEL)
        view.findNavController().navigate(action)

        searchViewModel.addPostViewCount()
    }



}