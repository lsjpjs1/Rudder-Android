package com.rudder.ui.fragment.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentMyPostHeaderBinding
import com.rudder.databinding.FragmentPostMessageRoomHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.postmessage.SendPostMessageDialogFragment
import com.rudder.util.SendPostMessageCallback
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import com.rudder.viewModel.PostMessageRoomViewModel

class MyPostHeaderFragment : Fragment() {


    val args by lazy {
        (parentFragment as MyPostDisplayFragment).args
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val header = DataBindingUtil.inflate<FragmentMyPostHeaderBinding>(inflater,
            R.layout.fragment_my_post_header,container,false)

        header.myPostHeaderLeftEmptySpace.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }
        if (args.viewModelIndex == MyPostDisplayFragment.MY_COMMENT){
            header.myPostHeaderTitleTV.text = "My Comments"
        }else if (args.viewModelIndex == MyPostDisplayFragment.MY_POST){
            header.myPostHeaderTitleTV.text = "My Posts"
        }


        return header.root
    }



}