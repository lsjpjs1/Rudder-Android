package com.rudder.ui.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.rudder.R
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class CommunityFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val community = inflater.inflate(R.layout.fragment_community, container, false)
        childFragmentManager.beginTransaction().add(R.id.communityDisplay,CommunityDisplayFragment(parentFragmentManager)).add(R.id.communityHeader,CommunityHeaderFragment()).commit()
        return community
    }
}