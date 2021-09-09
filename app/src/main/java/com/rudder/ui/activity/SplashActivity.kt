package com.rudder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivitySplashBinding
import com.rudder.util.ForecdTerminationService
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, ForecdTerminationService::class.java))

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
                    StartActivityUtil.callActivity(this, LoginActivity())
                    finish()
                }
            }
        })
        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, MainActivity())
                finish()
            }
        })

        autoLogin()
    }

    fun autoLogin(){
        GlobalScope.launch {
            val autoLoginPref = App.prefs.getValue("autoLogin")
            if (autoLoginPref == "true") {
                viewModel.callLogin()
            }
            else {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    StartActivityUtil.callActivity(this@SplashActivity, LoginActivity())
                    finish()
                }, 1000)
            }
        }

    }


}