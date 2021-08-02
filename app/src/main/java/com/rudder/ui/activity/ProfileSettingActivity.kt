package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.rudder.R
import com.rudder.databinding.ActivityProfileSettingBinding
import com.rudder.viewModel.ProfileSettingViewModel

class ProfileSettingActivity : AppCompatActivity() {

    private val viewModel: ProfileSettingViewModel = ProfileSettingViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityProfileSettingBinding>(this, R.layout.activity_profile_setting)
        binding.profileSettingVM = viewModel
        binding.lifecycleOwner = this


    }



}