package com.rudder.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.util.Event
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var viewModel: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Rudder)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

        autoLoginCheck()

        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Log.d("onDestoryLogin","${it}")
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                }
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

        Log.d("mytag","onDestoryLogin")
        val autoLoginPref = prefs.getValue("autoLogin")
        Log.d("autoLoginPref","$autoLoginPref")
        autoLoginCheckbox.isChecked = autoLoginPref == "true"
        val key = BuildConfig.TOKEN_KEY

        if (autoLoginPref == "false") { // 자동로그인을 안하고, 앱을 종료하면, 토큰 사라짐(로그아웃)
            Log.d("autoLoginPref2", "$autoLoginPref")
             prefs.removeValue(key)
             Log.d("autoLoginPref3","${prefs.getValue(key)}")
        }
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

    fun autoLoginCheck(){
        val autoLoginPref = prefs.getValue("autoLogin")
        Log.d("autoLoginPref","$autoLoginPref")
        autoLoginCheckbox.isChecked = autoLoginPref == "true"

        if (autoLoginPref == "true") {
            Log.d("autoLoginPref2","$autoLoginPref")
            viewModel.callLogin()
            }
    }



}