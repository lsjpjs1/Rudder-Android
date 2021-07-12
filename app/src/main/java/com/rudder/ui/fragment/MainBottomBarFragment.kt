package com.rudder.ui.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rudder.R
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class MainBottomBarFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomBar = inflater.inflate(R.layout.fragment_main_bottom_bar, container, false)
        bottomBar.communityButton.setOnClickListener {
            val purpleRudder = ContextCompat.getColor(requireContext(),R.color.purple_rudder)
            val grey = ContextCompat.getColor(requireContext(),R.color.grey)
            bottomBar.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
            bottomBar.myPageIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
        }
        bottomBar.myPageButton.setOnClickListener {
            val purpleRudder = ContextCompat.getColor(requireContext(),R.color.purple_rudder)
            val grey = ContextCompat.getColor(requireContext(),R.color.grey)
            bottomBar.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
            bottomBar.communityIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
        }
        return bottomBar
    }
}