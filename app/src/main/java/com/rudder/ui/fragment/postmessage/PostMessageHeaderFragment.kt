package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentMyPageHeaderBinding
import com.rudder.databinding.FragmentPostMessageHeaderBinding
import com.rudder.viewModel.MainViewModel

class PostMessageHeaderFragment : Fragment() {

    private val viewModel :MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val header = DataBindingUtil.inflate<FragmentPostMessageHeaderBinding>(inflater,R.layout.fragment_post_message_header,container,false)
        header.mainVM = viewModel
        return header.root
    }
}