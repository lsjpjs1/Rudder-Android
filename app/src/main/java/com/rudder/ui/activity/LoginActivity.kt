package com.rudder.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private var viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Rudder)


        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this
        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this, R.string.login_error,Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                callMainActivity()
            }
        })
        viewModel.startSignUpActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                callSignUpActivity()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestoryLogin","onDestory")
    }

    fun callMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun callSignUpActivity() {
        //finish()
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }



}