package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.rudder.R
import com.rudder.databinding.FragmentShowPostDisplayBinding


class ShowPostDisplayFragment : Fragment() {

    companion object{
        const val TAG = "ShowPostDisplayFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val showPostDisplayBinding= DataBindingUtil.inflate<FragmentShowPostDisplayBinding>(inflater,
            R.layout.fragment_show_post_display,container,false)

        showPostDisplayBinding.lifecycleOwner = this

        Log.d("createview_showpost","createview_showpost")
        return showPostDisplayBinding.root

    }


}