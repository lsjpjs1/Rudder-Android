package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.data.local.App.Companion.prefs
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class LoginActivity : AppCompatActivity() {
    //private var viewModel: LoginViewModel = LoginViewModel()
    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

        autoLoginCheckbox()

        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it)
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, MainActivity() )
                finish()
            }
        })
        viewModel.startSignUpActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, SignUpActivity())
            }
        })

        viewModel.startForgotActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, ForgotActivity())
            }
        })


        ProgressBarUtil.progressBarFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it){
                    ProgressBarUtil.progressBarVisible(progressBarLogin,loginContainer,R.color.transparent, this)
                }
                else
                    ProgressBarUtil.progressBarGone(progressBarLogin,loginContainer,R.color.white, this)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mytag","onDestoryLogin")
    }



    fun autoLoginCheckbox(){
        val autoLoginPref = prefs.getValue("autoLogin")
        Log.d("autoLoginPref","$autoLoginPref")
        autoLoginCheckbox.isChecked = autoLoginPref == "true"
    }
}