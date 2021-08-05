package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.FragmentProfileSettingBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_profile_setting.*
import kotlinx.android.synthetic.main.fragment_profile_setting.view.*


class ProfileSettingFragment : Fragment() {

    //private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var profileSettingBinding : FragmentProfileSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileSettingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_setting,container,false)
        profileSettingBinding.signUpVM = viewModel

        val toastNickName = Toast.makeText(activity, "Write Right Nickname !", Toast.LENGTH_SHORT)

        viewModel.nickNameFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(nickNameCheckbox)
                    toastNickName.cancel()
                     }
                else{
                    ChangeUIState.changeCheckBoxFalseState(nickNameCheckbox)
                    toastNickName.show()
                    }
                ChangeUIState.buttonEnable(signUpFinishBtn,nickNameCheckbox.isChecked)
            }})

        profileSettingBinding.root.introduceCheckbox.isChecked = true
        profileSettingBinding.root.introduceCheckbox.isEnabled = false
        profileSettingBinding.root.nickNameCheckbox.isEnabled = false
        profileSettingBinding.root.imageSettingCheckbox.isChecked = true
        profileSettingBinding.root.imageSettingCheckbox.isEnabled = false

        profileSettingBinding.root.signUpFinishBtn.isEnabled = false

        return profileSettingBinding.root
    }
}