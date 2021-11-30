package com.rudder.ui.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentAddPostBinding
import com.rudder.databinding.FragmentCommunityBinding
import com.rudder.databinding.FragmentCommunityPostBottomSheetBinding
import com.rudder.util.ProgressBarUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_post_display.view.*
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_community_header.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_show_post.*

class CommunityFragment: Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var community : FragmentCommunityBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val community = DataBindingUtil.inflate<FragmentCommunityBinding>(inflater,R.layout.fragment_community,container,false)

        childFragmentManager.beginTransaction()
            .add(R.id.communityDisplay,CommunityDisplayFragment(viewModel))
            .add(R.id.communityHeader,CommunityHeaderFragment())
            .add(R.id.communitySelector,CommunitySelectorFragment())
            .commit()

        community.mainVM = viewModel
        community.lifecycleOwner = this


//        ProgressBarUtil.progressBarMiniFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let { it ->
//                if (it)
//                    ProgressBarUtil.progressBarVisibleFragment(progressBarCommunityHeader, this)
//                else
//                    ProgressBarUtil.progressBarGoneFragment(progressBarCommunityHeader, this)
//            }
//        })



        return community.root
    }
}