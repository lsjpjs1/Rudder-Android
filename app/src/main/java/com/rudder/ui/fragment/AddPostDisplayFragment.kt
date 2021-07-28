package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.viewModel.MainViewModel

class AddPostDisplayFragment : Fragment() {
    private val viewModel = MainViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,
                R.layout.fragment_add_post_display,container,false)
        //header.mainVM = viewModel
        return header.root
    }
}