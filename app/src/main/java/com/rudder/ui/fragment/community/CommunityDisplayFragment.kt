package com.rudder.ui.fragment.community

import android.os.Bundle
import android.util.Log
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


        return community.root
    }
}