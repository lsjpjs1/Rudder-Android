package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
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

        header.constraintLayout13.setOnClickListener {
            Toast.makeText(lazyContext, "Coming soon!", Toast.LENGTH_SHORT).show()
        }

        return header.root
    }
}