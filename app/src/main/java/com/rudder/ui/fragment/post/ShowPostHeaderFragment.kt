package com.rudder.ui.fragment.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentShowPostHeaderBinding
import com.rudder.ui.activity.MainActivity

class ShowPostHeaderFragment() : Fragment() {



    @SuppressLint("RestrictedApi")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentShowPostHeaderBinding>(inflater,R.layout.fragment_show_post_header,container,false)
        //header.mainVM = viewModel
        header.lifecycleOwner = this

        header.constraintLayout13.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()

            if (navController.currentDestination!!.label == "SearchPostDisplayFragment") {
                (activity as MainActivity).nestedCommentDisappear()
            } else if (navController.currentDestination!!.label == "MyPostDisplayFragment") {
                (activity as MainActivity).nestedCommentDisappear()
            }
            else {
                (activity as MainActivity).mainBottomNavigationAppear()
                (activity as MainActivity).nestedCommentDisappear()
            }
        }


        return header.root
    }
}