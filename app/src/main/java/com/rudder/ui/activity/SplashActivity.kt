package com.rudder.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.databinding.ActivitySplashBinding
import com.rudder.viewModel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                    callLoginActivity()
                }
            }
        })
        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                callMainActivity()
            }
        })

        autoLogin()
    }

    fun autoLogin(){
        GlobalScope.launch {
            val autoLoginPref = App.prefs.getValue("autoLogin")
            Log.d("autoLoginPref","$autoLoginPref")
            if (autoLoginPref == "true") {
                viewModel.callLogin()
            }
            else {
                delay(1000L)
                callLoginActivity()
            }
        }

    }

    fun callMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestorySplash","onDestorySplash")
    }
}