package com.rudder.ui.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentEditPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class EditPostHeaderFragment(val viewModel: MainViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentEditPostHeaderBinding>(inflater,
            R.layout.fragment_edit_post_header,container,false)
        header.mainVM = viewModel
        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                if ((activity as MainActivity).validateBack("editPost")){
                    (activity as MainActivity).onBackPressed()
                }
            }
        })
        return header.root
    }
}