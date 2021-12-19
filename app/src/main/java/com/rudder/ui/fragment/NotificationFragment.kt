package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rudder.R
import com.rudder.databinding.FragmentNotificationBinding
import com.rudder.databinding.FragmentSearchPostBinding
import com.rudder.ui.activity.ActivityInterface
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.SearchViewModel

class NotificationFragment( val activityInterface: ActivityInterface) : Fragment() {

    private val viewModel: NotificationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentNotificationBinding>(inflater,
            R.layout.fragment_notification,container,false)

        fragmentBinding.lifecycleOwner = this



        return fragmentBinding.root
    }





}