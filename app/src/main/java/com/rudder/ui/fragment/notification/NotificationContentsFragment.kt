package com.rudder.ui.fragment.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentNotificationContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.NotificationAdapter
import com.rudder.ui.fragment.post.ShowPostDisplayFragment.Companion.NOTIFICATION_VIEW_MODEL
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragmentDirections
import com.rudder.util.NotificationAdapterCallback
import com.rudder.viewModel.NotificationViewModel


class NotificationContentsFragment : Fragment(), NotificationAdapterCallback {

    private val notificationViewModel : NotificationViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(NotificationViewModel::class.java)
    }

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val fragmentBinding = DataBindingUtil.inflate<FragmentNotificationContentsBinding>(inflater,
            R.layout.fragment_notification_contents,container,false)

        fragmentBinding.lifecycleOwner = this

        val notificationAdapter = NotificationAdapter(this, lazyContext)

        notificationViewModel.getNotifications()

        fragmentBinding.notificationDisplayRV.also {
            it.layoutManager =
                LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = notificationAdapter
        }


        notificationViewModel.notificationList.observe(viewLifecycleOwner, Observer {
            it?.let {
                notificationAdapter.submitList(it)
            }
        })


        return fragmentBinding.root
    }




    override fun onClickPostNotification(postId: Int) {
        notificationViewModel.getPostContentFromPostId(postId)
        notificationViewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            if (it == "Success") {
                val action = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationShowPost(notificationPostId = postId, viewModelIndex = NOTIFICATION_VIEW_MODEL)
                findNavController().navigate(action)
                (activity as MainActivity).mainBottomNavigationDisappear()
            } else {
                Toast.makeText(lazyContext,it, Toast.LENGTH_LONG).show()

            }

        })



//        val action = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationShowPost(notificationPostId = postId, viewModelIndex = NOTIFICATION_VIEW_MODEL)
//        findNavController().navigate(action)
//        (activity as MainActivity).mainBottomNavigationDisappear()
    }

    @SuppressLint("RestrictedApi")
    override fun onClickPostMessageRoomNotification(postMessageRoomId: Int) {
        val navController = findNavController()
        val actionNotificationToPostMessage = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationPostmessage(notificationPostMessageRoomId = postMessageRoomId)
        val actionPostMessageToPostMessageRoom = PostMessageDisplayFragmentDirections.actionNavigationPostmessageToNavigationPostmessageRoom(postMessageRoomId = postMessageRoomId)
        navController.navigate(actionNotificationToPostMessage)

        val mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            navController.navigate(actionPostMessageToPostMessageRoom)
        }, 300) // delay를 주지 않으면, postmessage와 postmessageRoom 두 개의 view가 바로 그려져서 겹쳐져 보이게 되기에 delay를 줌.


        (activity as MainActivity).mainBottomNavigationDisappear()
    }


}