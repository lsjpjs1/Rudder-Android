package com.rudder.ui.fragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentNotificationContentsBinding
import com.rudder.databinding.FragmentNotificationDisplayBinding
import com.rudder.ui.adapter.EditProfileImagesAdapter
import com.rudder.ui.adapter.NotificationAdapter
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.PostMessageRoomViewModel


class NotificationContentsFragment : Fragment() {
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

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }



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

        val notificationAdapter = NotificationAdapter()


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