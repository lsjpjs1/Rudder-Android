package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageHeaderBinding
import com.rudder.databinding.FragmentPostMessageRoomHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.util.SendPostMessageCallback
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.PostMessageRoomViewModel

class PostMessageRoomHeaderFragment:Fragment() {

    private val viewModel : PostMessageRoomViewModel by lazy {
        ViewModelProvider(parentFragment as ViewModelStoreOwner).get(PostMessageRoomViewModel::class.java)
    }
    private lateinit var sendPostMessageDialogFragment: SendPostMessageDialogFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val header = DataBindingUtil.inflate<FragmentPostMessageRoomHeaderBinding>(inflater,
            R.layout.fragment_post_message_room_header,container,false)
        header.postMessageRoomHeaderFragment = this

        header.postMessageRoomHeaderLeftEmptySpace.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }

        return header.root
    }

//    fun goBack(){
//        activity?.onBackPressed()
//    }

    fun showSendPostMessageDialog() {
        val receiveUserInfoId = viewModel.targetUserInfoId
        sendPostMessageDialogFragment = SendPostMessageDialogFragment(receiveUserInfoId.value,parentFragment as SendPostMessageCallback)
        sendPostMessageDialogFragment.show(childFragmentManager, "sendPostMessageDialogFragment")
    }
}