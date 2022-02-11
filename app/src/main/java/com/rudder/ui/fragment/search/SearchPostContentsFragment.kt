package com.rudder.ui.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.SearchViewModel

class SearchPostContentsFragment  : Fragment(),CustomOnclickListener {

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy{
        activity as MainActivity
    }

//    private val viewModel: SearchViewModel by activityViewModels()
    private val viewModel : SearchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchDisplayBinding = DataBindingUtil.inflate<FragmentSearchPostContentsBinding>(inflater,
            R.layout.fragment_search_post_contents,container,false)
        searchDisplayBinding.lifecycleOwner = this

        val adapter = MainPostPreviewAdapter(this,lazyContext, viewModel,viewLifecycleOwner)
        adapter.submitList(viewModel.posts.value!!.toMutableList().map { it.copy() })
        searchDisplayBinding.searchPostPreviewRV.also{
            it.layoutManager= LinearLayoutManager(lazyContext)
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

        viewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                    (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
            }
//            it?.let {
//                if(it)
//                (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
//            }
        })

        viewModel.posts.observe(viewLifecycleOwner, Observer { items ->
            adapter.submitList(items.toMutableList().map { it.copy() })
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
                    //viewModel.searchPost(false)
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
                    //viewModel.searchPost(false)
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

        return searchDisplayBinding.root
    }

    override fun onClickView(view: View, position: Int) {
        //viewModel.setSelectedPostPosition(position)
//        (activity as MainActivity).showPost(viewModel, ShowPostContentsFragment(viewModel))

//        (activity as MainActivity).showPost(viewModel, ShowPostContentsFragment(viewModel))
        (activity as MainActivity).changeSelectedPostPosition(position)

        //viewModel.


        viewModel.setPostTmp(position)




        view.findNavController().navigate(R.id.action_navigation_search_to_navigation_show_post)
        Log.d("test","test")
        //(activity as MainActivity).mainBottomNavigationAppear()
        //(activity as MainActivity).addCommentFragmentAppear()
        (activity as MainActivity).addCommentMainBottomLayoutAppear()


        //(activity as MainActivity).showAddComment(AddCommentFragment(viewModel))
//        if(!viewModel.isAlreadyReadPost()){
//            viewModel.addPostViewCount()
//        }
        viewModel.addPostViewCount()
        viewModel.getComments()
    }



}