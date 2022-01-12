package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentNotificationBinding
import com.rudder.databinding.FragmentPostMessageBinding
import com.rudder.ui.adapter.PostMessageAdapter
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.PostMessageViewModel

class PostMessageFragment : Fragment() {

    private val viewModel : PostMessageViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentPostMessageBinding>(inflater,
            R.layout.fragment_post_message,container,false)

        fragmentBinding.lifecycleOwner = this

        val adapter = PostMessageAdapter()
        adapter.submitList(
            viewModel.getPostMessages()
        )

        fragmentBinding.postMessageDisplayRV.also {
            it.layoutManager =
                LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }



        return fragmentBinding.root
    }


}