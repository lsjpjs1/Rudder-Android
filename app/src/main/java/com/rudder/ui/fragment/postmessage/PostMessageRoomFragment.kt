package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageRoomBinding
import com.rudder.ui.adapter.RoomPostMessagesAdapter
import com.rudder.util.SendPostMessageCallback
import com.rudder.viewModel.PostMessageRoomViewModel

class PostMessageRoomFragment : Fragment(),SendPostMessageCallback {
    private val viewModel: PostMessageRoomViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }


    private val adapter by lazy {
        RoomPostMessagesAdapter(lazyContext)
    }

    lateinit var fragmentBinding: FragmentPostMessageRoomBinding
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
        viewModel.setPostMessageRoomId(args.postMessageRoomId)
        fragmentBinding = DataBindingUtil.inflate<FragmentPostMessageRoomBinding>(inflater,
                R.layout.fragment_post_message_room, container, false)

        fragmentBinding.lifecycleOwner = this
        fragmentBinding.postMessageRoomFragment = this

        fragmentBinding.roomPostMessageRV.also {
            it.layoutManager =
                    LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        viewModel.getMessagesByRoom()

        viewModel.messages.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it){
                    fragmentBinding.roomPostMessageRV.scrollToPosition(0)
                }
            }
            fragmentBinding.roomPostMessageSwipeRefreshLayout.isRefreshing=false
        })

        fragmentBinding.roomPostMessageSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        fragmentBinding.roomPostMessageSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getMessagesByRoom()
            }

        })

        return fragmentBinding.root
    }

    fun showSendPostMessageDialog() {
        val receiveUserInfoId = viewModel.targetUserInfoId
        sendPostMessageDialogFragment = SendPostMessageDialogFragment(receiveUserInfoId.value,this)
        sendPostMessageDialogFragment.show(childFragmentManager, "sendPostMessageDialogFragment")
    }

    override fun onPostMessageSend() {
        viewModel.getMessagesByRoom()
        findNavController().previousBackStackEntry?.savedStateHandle?.set("onMessageSend", true)
    }

}