package com.rudder.ui.fragment.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.databinding.FragmentNotificationHeaderBinding
import com.rudder.ui.activity.MainActivity


class NotificationHeaderFragment : Fragment() {

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val notificationHeaderBinding = DataBindingUtil.inflate<FragmentNotificationHeaderBinding>(inflater, R.layout.fragment_notification_header, container, false)

        notificationHeaderBinding.notificationBackButtonCL.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }

        return notificationHeaderBinding.root

    }

}