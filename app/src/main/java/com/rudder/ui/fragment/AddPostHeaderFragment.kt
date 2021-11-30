package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.databinding.FragmentShowPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class AddPostHeaderFragment : Fragment() {
    private val viewModel :MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentAddPostHeaderBinding>(inflater,
            R.layout.fragment_add_post_header,container,false)
        header.mainVM = viewModel

        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                if ((activity as MainActivity).validateBack("addPost")){
                    (activity as MainActivity).onBackPressed()
                }
            }
        })
        return header.root
    }
}