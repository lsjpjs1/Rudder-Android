package com.rudder.ui.fragment.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.navArgs
import com.rudder.R
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.databinding.FragmentMyPostDisplayBinding
import com.rudder.ui.fragment.post.EditPostFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragmentArgs
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import com.rudder.viewModel.SearchViewModel

class MyPostDisplayFragment : Fragment() {


    companion object{
        const val MY_COMMENT = 2
        const val MY_POST = 1
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

//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if ((activity as MainActivity).validateBack("community")){
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        })

        return binding.root
    }

}