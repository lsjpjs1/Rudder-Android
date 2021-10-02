package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentSearchPostBinding
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.MainViewModel

class SearchPostFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentSearchPostBinding>(inflater,
            R.layout.fragment_search_post,container,false)

        fragmentBinding.lifecycleOwner = this
        childFragmentManager.beginTransaction()
            .add(R.id.searchPostHeader,SearchPostHeaderFragment())
            .add(R.id.searchPostDisplay,SearchPostDisplayFragment())
            .commit()


        return fragmentBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("destroy","dest")
    }
}