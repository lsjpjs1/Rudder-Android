package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rudder.R
import com.rudder.databinding.FragmentNotificationBinding
import com.rudder.databinding.FragmentPostMessageBinding
import com.rudder.viewModel.NotificationViewModel

class PostMessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentPostMessageBinding>(inflater,
            R.layout.fragment_post_message,container,false)

        fragmentBinding.lifecycleOwner = this



        return fragmentBinding.root
    }


}