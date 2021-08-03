package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.ui.fragment.CreateAccountFragment
import com.rudder.ui.fragment.SchoolSelectFragment
import com.rudder.viewModel.SignUpViewModel


class SignUpActivity2 : AppCompatActivity() {
    private var viewModel: SignUpViewModel = SignUpViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up2)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this


        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.signUp_container, SchoolSelectFragment())
        transaction.commit()

//        viewModel.schoolSelectNext.observe(this, Observer {
//            transaction.add(R.id.signUp_container, CreateAccountFragment())
//            transaction.commit()
//        })




    }
}