package com.rudder.ui.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rudder.R
import com.rudder.databinding.FragmentNotificationDisplayBinding
import com.rudder.ui.activity.ActivityInterface
import com.rudder.viewModel.NotificationViewModel

class NotificationDisplayFragment() : Fragment() {

    private val notificationViewModel: NotificationViewModel by viewModels()

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





}