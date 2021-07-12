package com.rudder.ui.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.viewModel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private val viewModel: SignUpViewModel = SignUpViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this
        viewModel.userPassword.observe(this, Observer {
            if(it==viewModel.userPasswordCheck.value && it.isNotBlank()){
                Toast.makeText(this,"same",Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.userPasswordCheck.observe(this, Observer {
            if(it==viewModel.userPassword.value && it.isNotBlank()){
                Toast.makeText(this,"same",Toast.LENGTH_SHORT).show()
            }
        })
    }
}