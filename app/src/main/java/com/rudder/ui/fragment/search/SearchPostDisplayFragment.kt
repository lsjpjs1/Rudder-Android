package com.rudder.ui.fragment.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostDisplayBinding
import com.rudder.viewModel.SearchViewModel

class SearchPostDisplayFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback
    private val viewModel: SearchViewModel by activityViewModels()
    companion object{
        const val TAG = "SearchPostDisplayFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentSearchPostDisplayBinding>(inflater,
            R.layout.fragment_search_post_display,container,false)

        fragmentBinding.lifecycleOwner = this




        return fragmentBinding.root
    }


    override fun onResume() {
        viewModel.clearSearchPost() // searchviewmodel의 posts를 clear
        super.onResume()
    }
}


