package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.viewModel.MainViewModel

class CommunityHeaderFragment : Fragment() {
    private val viewModel = MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val header = DataBindingUtil.inflate<FragmentCommunityHeaderBinding>(inflater,R.layout.fragment_community_header,container,false)
        header.mainVM = viewModel
        return header.root
    }
}