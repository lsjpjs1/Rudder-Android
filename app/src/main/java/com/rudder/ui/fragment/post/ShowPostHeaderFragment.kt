package com.rudder.ui.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentShowPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class ShowPostHeaderFragment(val viewModel: MainViewModel) : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentShowPostHeaderBinding>(inflater,R.layout.fragment_show_post_header,container,false)
        header.mainVM = viewModel
        header.lifecycleOwner = this
        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                if ((activity as MainActivity).validateBack("showPost")){
                    (activity as MainActivity).onBackPressed()
                }
            }

        })
        return header.root
    }
}