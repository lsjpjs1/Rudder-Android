package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.SignUpViewModel


class SignUpActivity : AppCompatActivity() {
    private var viewModel: SignUpViewModel = SignUpViewModel

    private lateinit var createAccountFragment : CreateAccountFragment
    private lateinit var profileSettingFragment : ProfileSettingFragment
    private lateinit var schoolSelectFragment: SchoolSelectFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this

        createAccountFragment = CreateAccountFragment()
        profileSettingFragment = ProfileSettingFragment()
        schoolSelectFragment = SchoolSelectFragment()


//        val transaction = supportFragmentManager.beginTransaction()
//            .add(R.id.signUp_container, createAccountFragment)
//            .hide(createAccountFragment)
//            .add(R.id.signUp_container, profileSettingFragment)
//            .hide(profileSettingFragment)
//            .add(R.id.signUp_container, schoolSelectFragment)

        //transaction.commit()

        viewModel.schoolSelectNext.observe(this, Observer {
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.showFragment(createAccountFragment,R.id.signUp_container)
        })

    }

    override fun onResume() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.signUp_container, createAccountFragment)
            .hide(createAccountFragment)
            .add(R.id.signUp_container, profileSettingFragment)
            .hide(profileSettingFragment)
            .add(R.id.signUp_container, schoolSelectFragment)

        transaction.commit()
        super.onResume()
    }
}