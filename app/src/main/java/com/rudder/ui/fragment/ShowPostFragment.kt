package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentShowPostBinding
import com.rudder.viewModel.MainViewModel

class ShowPostFragment: Fragment() {
    private val viewModel = MainViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding= DataBindingUtil.inflate<FragmentShowPostBinding>(inflater,R.layout.fragment_show_post,container,false)
        fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        return fragmentBinding.root
    }
}