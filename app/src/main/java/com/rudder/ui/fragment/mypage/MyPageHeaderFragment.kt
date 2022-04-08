package com.rudder.ui.fragment.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentMyPageHeaderBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class MyPageHeaderFragment : Fragment() {

    private val mainViewModel :MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val headerDataBinding = DataBindingUtil.inflate<FragmentMyPageHeaderBinding>(inflater,R.layout.fragment_my_page_header,container,false)
        headerDataBinding.mainVM = mainViewModel

        headerDataBinding.settingButtonCL.setOnClickListener{ view ->
            val navController = view.findNavController()
            val action = MyPageDisplayFragmentDirections.actionNavigationMypageToNavigationSetting()
            navController.navigate(action)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }


        return headerDataBinding.root
    }
}