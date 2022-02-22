package com.rudder.ui.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SearchViewModel

class SearchPostHeaderFragment : Fragment() {

//    private val viewModel: SearchViewModel by activityViewModels()
    private val viewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val header = DataBindingUtil.inflate<FragmentSearchPostHeaderBinding>(inflater,
            R.layout.fragment_search_post_header,container,false)
        header.mainVM = viewModel
        header.lifecycleOwner = this

        header.searchPostSearchView.gravity = Gravity.RIGHT



        header.searchPostSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    viewModel.searchPost(false)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    viewModel.setSearchWord(it)
                }
                return true
            }
        })


        viewModel.isScrollTouch.observe(viewLifecycleOwner, Observer {
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


//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                if ((activity as MainActivity).validateBack("searchPost")){
//                    viewModel.clearSearchPost()
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        })


        header.searchPostHeaderX.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()

        }


        return header.root
    }

}