package com.rudder.ui.fragment.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rudder.R
import com.rudder.databinding.FragmentMyPostDisplayBinding

class MyPostDisplayFragment : Fragment() {

    companion object{
        const val TAG = "MyPostDisplayFragment"
        const val MY_POST = 1
        const val MY_COMMENT = 2
    }
    val args : MyPageDisplayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMyPostDisplayBinding>(inflater,
            R.layout.fragment_my_post_display,container,false)


        binding.lifecycleOwner = this


        return binding.root
    }

}