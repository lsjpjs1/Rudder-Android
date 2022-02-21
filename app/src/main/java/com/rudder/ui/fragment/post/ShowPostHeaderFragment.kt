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

//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if ((activity as MainActivity).validateBack("showPost")){
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//
//        })
        header.constraintLayout13.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()

            Log.d("1231234","${navController.currentDestination!!.label}")

            if (navController.currentDestination!!.label == "SearchPostDisplayFragment") {
                (activity as MainActivity).nestedCommentDisappear()
            } else if (navController.currentDestination!!.label == "MyPostDisplayFragment") {
                Log.d("123123","456456")

                (activity as MainActivity).mainBottomNavigationDisappear()
                (activity as MainActivity).nestedCommentDisappear()
            }
            else {
                    Log.d("123123","123123")
                (activity as MainActivity).mainBottomNavigationAppear()
                (activity as MainActivity).nestedCommentDisappear()
            }
        }


        return header.root
    }
}