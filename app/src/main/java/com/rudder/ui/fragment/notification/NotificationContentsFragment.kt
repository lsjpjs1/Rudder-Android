package com.rudder.ui.fragment.notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.rudder.ui.fragment.post.ShowPostDisplayFragment.Companion.MAIN_VIEW_MODEL
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragmentDirections
import com.rudder.util.NotificationAdapterCallback
import com.rudder.viewModel.NotificationViewModel


class NotificationContentsFragment : Fragment(), NotificationAdapterCallback {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    private val notificationViewModel : NotificationViewModel by lazy {
        ViewModelProvider(parentFragment as ViewModelStoreOwner).get(NotificationViewModel::class.java)
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

        val notificationAdapter = NotificationAdapter(this)


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
        val action = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationShowPost(notificationPostId = postId, viewModelIndex = NotificationViewModel)
        findNavController().navigate(action)


        notificationViewModel.getPostContentFromPostId(postId)

        (activity as MainActivity).mainBottomNavigationDisappear()
    }

    @SuppressLint("RestrictedApi")
    override fun onClickPostMessageRoomNotification(postMessageRoomId: Int) {
        //val action = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationPostmessageRoom(notificationPostMessageRoomId = postMessageRoomId)
        val navController = findNavController()


        val actionNotificationToPostMessage = NotificationDisplayFragmentDirections.actionNavigationNotificationToNavigationPostmessage(notificationPostMessageRoomId = postMessageRoomId)
        val actionPostMessageToPostMessageRoom = PostMessageDisplayFragmentDirections.actionNavigationPostmessageToNavigationPostmessageRoom(postMessageRoomId = postMessageRoomId)
        navController.navigate(actionNotificationToPostMessage)

        val mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed({
            navController.navigate(actionPostMessageToPostMessageRoom)
        }, 100) // delay를 주지 않으면, postmessage와 postmessageRoom 두 개의 view가 바로 그려져서 겹쳐져 보이게 되기에 delay를 줌.


        (activity as MainActivity).mainBottomNavigationDisappear()
    }


//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment NotificationContentsFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            NotificationContentsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}