package com.rudder.ui.fragment.community

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class CommunityHeaderFragment : Fragment() {

    private val viewModel :MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val header = DataBindingUtil.inflate<FragmentCommunityHeaderBinding>(inflater,R.layout.fragment_community_header,container,false)
        header.mainVM = viewModel

//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if ((activity as MainActivity).validateBack("community")){
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        })


        viewModel.isScrollBottomTouch.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled().let {
                it?.let{
                    if (it){
                        header.progressBarCommunityHeader.visibility = View.VISIBLE
                    } else {
                        header.progressBarCommunityHeader.visibility = View.INVISIBLE
                    }
                }
            }
        })



        header.constraintLayout13.setOnClickListener { view -> // search button click
            view.findNavController().navigate(R.id.action_navigation_community_to_navigation_search)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }


        header.constraintLayout12.setOnClickListener { view -> // add post button click
            (activity as MainActivity).mainBottomNavigationDisappear()
        }



        return header.root
    }
}