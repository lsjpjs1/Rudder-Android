package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.databinding.FragmentMypageHeaderBinding
import com.rudder.viewModel.MainViewModel

class MyPageHeaderFragment : Fragment() {

    private val viewModel :MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val header = DataBindingUtil.inflate<FragmentMypageHeaderBinding>(inflater,R.layout.fragment_mypage_header,container,false)
        header.mainVM = viewModel
        return header.root
    }
}