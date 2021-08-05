package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.databinding.FragmentProfileSettingBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.viewModel.SignUpViewModel


class ProfileSettingFragment : Fragment() {

    //private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }

    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var profileSetting : FragmentProfileSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileSetting = DataBindingUtil.inflate<FragmentProfileSettingBinding>(inflater,R.layout.fragment_profile_setting,container,false)
        profileSetting.signUpVM = viewModel

        return profileSetting.root
    }
}