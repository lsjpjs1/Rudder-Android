package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageBinding
import com.rudder.databinding.FragmentPostMessageRoomBinding
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.ui.adapter.PostMessageAdapter
import com.rudder.viewModel.PostMessageRoomViewModel
import com.rudder.viewModel.PostMessageViewModel

class PostMessageRoomFragment : Fragment(){
    private val viewModel: PostMessageRoomViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = DataBindingUtil.inflate<FragmentPostMessageRoomBinding>(inflater,
                R.layout.fragment_post_message_room, container, false)

        fragmentBinding.lifecycleOwner = this




        return fragmentBinding.root
    }



}