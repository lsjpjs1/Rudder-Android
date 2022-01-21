package com.rudder.ui.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.viewModel.MainViewModel

class CommunityDisplayFragment: Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var community : FragmentCommunityDisplayBinding

    companion object{
        const val TAG = "CommunityDisplayFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val community = DataBindingUtil.inflate<FragmentCommunityDisplayBinding>(inflater,R.layout.fragment_community_display,container,false)

        community.mainVM = viewModel
        community.lifecycleOwner = this

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
//            .add(R.id.communityDisplay, CommunityContentsFragment(viewModel))
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