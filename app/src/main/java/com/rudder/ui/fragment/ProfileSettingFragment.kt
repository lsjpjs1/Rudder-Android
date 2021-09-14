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
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentProfileSettingBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.activity.SignUpActivity
import com.rudder.ui.adapter.DisplayImagesAdapter
import com.rudder.ui.adapter.ProfileSettingImagesAdapter
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_profile_setting.*
import kotlinx.android.synthetic.main.fragment_profile_setting.view.*
import kotlinx.android.synthetic.main.profile_setting_display_image.*


class ProfileSettingFragment : Fragment() {

    //private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }
    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var profileSettingBinding : FragmentProfileSettingBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileSettingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_setting,container,false)
        profileSettingBinding.signUpVM = viewModel
        profileSettingBinding.lifecycleOwner = this

        val toastNickName = Toast.makeText(activity, "NickName (4-15 characters) can be numbers, upper or lower letters.", Toast.LENGTH_LONG)


        val profileSettingImagesAdapter = ProfileSettingImagesAdapter(viewModel.profileImageList.value!!,  lazyContext, (activity as SignUpActivity).getDisplaySize(), viewModel, viewLifecycleOwner)

        profileSettingBinding.profileSettingRecyclerView.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext, HORIZONTAL, false){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(true)
            it.adapter = profileSettingImagesAdapter
        }


        viewModel.profileImageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                profileSettingImagesAdapter.imageUrlList = it
                profileSettingImagesAdapter.notifyDataSetChanged()
            }
        })

        viewModel.nickbnameRgCheck.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    nickNameDuplicatedCheck.isEnabled = true
                    toastNickName.cancel()
                     }
                else{
                    nickNameDuplicatedCheck.isEnabled = false
                    toastNickName.show()
                    }
                ChangeUIState.buttonEnable(profileSettingNextBtn, nickNameCheckbox.isChecked, imageSettingCheckbox.isChecked)
            }})


        viewModel.nickNameDuplicatedCheck.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    ChangeUIState.changeCheckBoxTrueState(nickNameCheckbox)
                    nickNameDuplicatedCheck.isEnabled = false
                } else {
                    ChangeUIState.changeCheckBoxFalseState(nickNameCheckbox)
                    Toast.makeText(activity, "NickName is duplicated", Toast.LENGTH_LONG).show()
                }
            }
            ChangeUIState.buttonEnable(profileSettingNextBtn, nickNameCheckbox.isChecked,imageSettingCheckbox.isChecked)
        })



        viewModel.nickNameChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(nickNameCheckbox)
                ChangeUIState.buttonEnable(profileSettingNextBtn, nickNameCheckbox.isChecked, imageSettingCheckbox.isChecked)
            }
        })


        viewModel.profileImageClick.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                imageSettingCheckbox.isChecked = it
            }
            ChangeUIState.buttonEnable(profileSettingNextBtn, nickNameCheckbox.isChecked,imageSettingCheckbox.isChecked)
        })



        profileSettingBinding.root.introduceCheckbox.isChecked = true
        profileSettingBinding.root.introduceCheckbox.isEnabled = false
        profileSettingBinding.root.nickNameCheckbox.isEnabled = false
        profileSettingBinding.root.imageSettingCheckbox.isEnabled = false

        profileSettingBinding.root.nickNameDuplicatedCheck.isEnabled = false
        profileSettingBinding.root.profileSettingNextBtn.isEnabled = false

        return profileSettingBinding.root
    }
}