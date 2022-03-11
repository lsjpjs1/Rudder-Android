package com.rudder.ui.fragment.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentNotificationDisplayBinding
import com.rudder.ui.activity.ActivityInterface
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.post.CommunityPostBottomSheetFragment
import com.rudder.util.Event
import com.rudder.viewModel.NotificationViewModel

class NotificationDisplayFragment : Fragment() {

    private val notificationViewModel: NotificationViewModel by activityViewModels()
    companion object{
        const val TAG = "NotificationDisplayFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentNotificationDisplayBinding>(inflater,
            R.layout.fragment_notification_display,container,false)

        fragmentBinding.lifecycleOwner = this

        return fragmentBinding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            notificationViewModel.getNotifications()
        }
        super.onHiddenChanged(hidden)
    }



}