package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.MainPostPreviewAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_community_display.view.*
import kotlinx.coroutines.launch

class SearchPostDisplayFragment  : Fragment(),CustomOnclickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchDisplayBinding = DataBindingUtil.inflate<FragmentSearchPostDisplayBinding>(inflater,
            R.layout.fragment_search_post_display,container,false)
        searchDisplayBinding.lifecycleOwner = this

        val adapter = MainPostPreviewAdapter(this,lazyContext, viewModel)
        adapter.submitList(viewModel.searchPosts.value!!.toMutableList().map { it.copy() })
        searchDisplayBinding.searchPostPreviewRV.also{
            it.layoutManager= LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }


        viewModel.searchPosts.observe(viewLifecycleOwner, Observer { items ->
            adapter.submitList(items.toMutableList().map { it.copy() })
        })
        return searchDisplayBinding.root
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