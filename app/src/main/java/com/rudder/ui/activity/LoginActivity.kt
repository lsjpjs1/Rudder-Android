package com.rudder.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.util.ActivityContainer
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }

    private fun hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityContainer.currentActivity = this
        App.prefs.removeValue(BuildConfig.TOKEN_KEY)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        autoLoginCheckbox()

        loginContainer.setOnClickListener {
            hideSoftKeyboard()
        }



        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it)

                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                ProgressBarUtil._progressBarFlag.postValue(Event(false))

            }
        })
        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, MainActivity() )
                finish()
                ProgressBarUtil._progressBarFlag.postValue(Event(false))
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
                if (it)
                    ProgressBarUtil.progressBarVisibleActivity(progressBarLogin, this)
                else
                    ProgressBarUtil.progressBarGoneActivity(progressBarLogin, this)
            }
        })

        if(viewModel.noticeResponse.value==null){
            viewModel.getNotice()
        }

        viewModel.noticeResponse.observe(this, Observer {
            it?.let {
                if(it.isExist){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Notice").setMessage(it.notice)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                    App.prefs.setValue("isNoticeAlreadyPopUp","true")
                }

            }
        })


    }
    override fun onDestroy() {
        ActivityContainer.clearCurrentActivity(this)
        super.onDestroy()
    }

    fun autoLoginCheckbox(){
        val autoLoginPref = prefs.getValue("autoLogin")
        autoLoginCheckbox.isChecked = autoLoginPref == "true"
    }
}