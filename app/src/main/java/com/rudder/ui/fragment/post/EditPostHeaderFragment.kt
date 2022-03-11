package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentEditPostHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class EditPostHeaderFragment(val viewModel: MainViewModel) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentEditPostHeaderBinding>(inflater, R.layout.fragment_edit_post_header,container,false)
        header.mainVM = viewModel

        header.addPostHeaderX.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()
            if (navController.currentDestination!!.label == "Community") {
                (activity as MainActivity).mainBottomNavigationAppear()
            }
        }

        return header.root
    }
}