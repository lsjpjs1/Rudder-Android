package com.rudder.ui.fragment.mypage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentMyPostHeaderBinding
import com.rudder.databinding.FragmentSettingBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.SettingViewModel

class SettingFragment : Fragment() {

    companion object {
        fun newInstance() = SettingFragment()
        const val TAG = "SettingFragment"
    }

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val settingDataBinding = DataBindingUtil.inflate<FragmentSettingBinding>(inflater, R.layout.fragment_setting,container,false)


        settingDataBinding.settingBackBtn.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }


        return settingDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}