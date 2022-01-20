package com.rudder.ui.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentCommunityBinding
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class CommunityFragment: Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var community : FragmentCommunityBinding



    companion object{
        const val TAG = "CommunityFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val community = DataBindingUtil.inflate<FragmentCommunityBinding>(inflater,R.layout.fragment_community,container,false)

        childFragmentManager.beginTransaction()
            .add(R.id.communityDisplay, CommunityDisplayFragment(viewModel))
            //.add(R.id.communityHeader, CommunityHeaderFragment())
            .add(R.id.communitySelector, CommunitySelectorFragment())
            .commit()

        community.mainVM = viewModel
        community.lifecycleOwner = this


        val communityTmp = DataBindingUtil.inflate<FragmentCommunityHeaderBinding>(inflater,R.layout.fragment_community_header,container,false)


        //communityTmp.

//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if ((activity as MainActivity).validateBack("community")){
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        })


        return community.root

//        val community = DataBindingUtil.inflate<FragmentCommunityBinding>(inflater,R.layout.fragment_community,container,false)
//
//        childFragmentManager.beginTransaction()
//            .add(R.id.communityDisplay, CommunityDisplayFragment(viewModel))
//            .add(R.id.communityHeader, CommunityHeaderFragment())
//            .add(R.id.communitySelector, CommunitySelectorFragment())
//            .commit()
//
//        community.mainVM = viewModel
//        community.lifecycleOwner = this


//        ProgressBarUtil.progressBarMiniFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let { it ->
//                if (it)
//                    ProgressBarUtil.progressBarVisibleFragment(progressBarCommunityHeader, this)
//                else
//                    ProgressBarUtil.progressBarGoneFragment(progressBarCommunityHeader, this)
//            }
//        })
        //return community.root
    }
}