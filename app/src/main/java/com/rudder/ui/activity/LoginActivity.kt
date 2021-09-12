package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }


    // id가 명시되어있지 않은 다른 부분을 터치했을 때 키보드가 보여져있는 상태면 키보드를 내림.
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }




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
                if (it)
                    ProgressBarUtil.progressBarVisibleActivity(progressBarLogin, this)
                else
                    ProgressBarUtil.progressBarGoneActivity(progressBarLogin, this)
            }
        })

    }




    fun autoLoginCheckbox(){
        val autoLoginPref = prefs.getValue("autoLogin")
        autoLoginCheckbox.isChecked = autoLoginPref == "true"
    }
}