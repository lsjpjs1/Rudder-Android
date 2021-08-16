package com.rudder.ui.activity

import android.content.Intent

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.util.Event
import com.rudder.util.FragmentShowHide
import com.rudder.util.StartActivity
import com.rudder.viewModel.LoginViewModel
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*


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
                StartActivity.callActivity(this, MainActivity() )
            }
        })
        viewModel.startSignUpActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivity.callActivity(this, SignUpActivity())
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