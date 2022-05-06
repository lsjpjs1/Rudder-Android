package com.rudder.ui.fragment.search

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.SearchViewModel

class SearchPostHeaderFragment : Fragment() {

    private val searchViewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val header = DataBindingUtil.inflate<FragmentSearchPostHeaderBinding>(inflater,
            R.layout.fragment_search_post_header,container,false)
        header.mainVM = searchViewModel
        header.lifecycleOwner = this
        header.searchPostSearchView.gravity = Gravity.RIGHT


        header.searchPostSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    searchViewModel.searchPost(false)
                }
                header.searchPostSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    searchViewModel.setSearchWord(it)
                }
                return true
            }
        })


        searchViewModel.isScrollTouch.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                it?.let{
                    if (it){
                        header.progressBarSearchPostHeader.visibility = View.VISIBLE
                    } else {
                        header.progressBarSearchPostHeader.visibility = View.INVISIBLE
                    }
                }
            }
        })


        header.searchPostHeaderX.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()

        }


        return header.root
    }

}