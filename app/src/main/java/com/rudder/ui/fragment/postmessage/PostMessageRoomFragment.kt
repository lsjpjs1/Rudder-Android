package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageRoomBinding
import com.rudder.ui.adapter.RoomPostMessagesAdapter
import com.rudder.viewModel.PostMessageRoomViewModel

class PostMessageRoomFragment : Fragment() {
    private val viewModel: PostMessageRoomViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }


    companion object{
        const val TAG = "PostMessageRoomFragment"
    }

    private lateinit var sendPostMessageDialogFragment: SendPostMessageDialogFragment
    val args : PostMessageRoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val fragmentBinding = DataBindingUtil.inflate<FragmentPostMessageRoomBinding>(inflater,
                R.layout.fragment_post_message_room, container, false)

        fragmentBinding.lifecycleOwner = this
        fragmentBinding.postMessageRoomFragment = this

        val adapter = RoomPostMessagesAdapter(lazyContext)
        fragmentBinding.roomPostMessageRV.also {
            it.layoutManager =
                    LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        val postMessageRoomIdValue = args.postMessageRoomId
        viewModel.getMessagesByRoom(postMessageRoomIdValue!!)

        viewModel.messages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
            fragmentBinding.roomPostMessageSwipeRefreshLayout.isRefreshing=false
        })
        fragmentBinding.roomPostMessageSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getMessagesByRoom(args.postMessageRoomId)
            }

        })

        return fragmentBinding.root
    }

    fun showSendPostMessageDialog() {
        val receiveUserInfoId = viewModel.targetUserInfoId
        sendPostMessageDialogFragment = SendPostMessageDialogFragment(receiveUserInfoId.value)
        sendPostMessageDialogFragment.show(childFragmentManager, "sendPostMessageDialogFragment")
    }

}