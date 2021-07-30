package com.rudder.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.viewModel.SignUpViewModel
import android.view.View
import android.text.TextWatcher
import android.widget.*
import kotlinx.android.synthetic.main.activity_sign_up.*



class SignUpActivity : AppCompatActivity() {
    private val viewModel: SignUpViewModel = SignUpViewModel()

    val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()
    val passwordRg = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)
    fun changeCheckBoxTrueState(checkBox: CheckBox){
        checkBox.isEnabled = true
        checkBox.isChecked = true
        checkBox.isEnabled = false
    }

    fun changeCheckBoxFalseState(checkBox: CheckBox){
        checkBox.isEnabled = true
        checkBox.isChecked = false
        checkBox.isEnabled = false
    }

    fun buttonEnable(b0 : Boolean, b1 : Boolean, b2 : Boolean, b3 : Boolean){
        verifyBtn.isEnabled = b0 && b1 && b2 && b3
    }


    fun callLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this

        viewModel.userPassword.observe(this, Observer {
            if (it.trim().matches(passwordRg) && it.isNotBlank()) changeCheckBoxTrueState(
                PWcheckbox1
            )
            else {
                changeCheckBoxFalseState(PWcheckbox1)
                Toast.makeText(this, "비밀번호는 숫자,문자,특수문자 중 2가지 포함(8~15자)", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
        })

        viewModel.userPasswordCheck.observe(this, Observer {
            if (it.trim() == viewModel.userPassword.value!!.trim() && it.isNotBlank()) {
                changeCheckBoxTrueState(PWcheckbox2)
            } else {
                changeCheckBoxFalseState(PWcheckbox2)
                Toast.makeText(
                    this,
                    "Please Check, Password and Password Confirm",
                    Toast.LENGTH_SHORT
                ).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
        })

        viewModel.userEmailDomain.observe(this, Observer {
            if (it.trim().matches(emailRg)) {
                changeCheckBoxTrueState(emailCheckbox)
            } else {
                changeCheckBoxFalseState(emailCheckbox)
                Toast.makeText(this, "Please Check, Right Email Domain", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
        })

        viewModel.idCheck.observe(this, Observer {
            if (!it) changeCheckBoxTrueState(IDcheckbox)
            else {
                changeCheckBoxFalseState(IDcheckbox)
                Toast.makeText(this, "ID is duplicated", Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.emailCheck.observe(this, Observer {
            if (it) submitBtn.isEnabled = true
            else {
                submitBtn.isEnabled = false
                Toast.makeText(this, "Email must be Naver Email", Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.verifyCodeCheck.observe(this, Observer {
            if (it) {
                changeCheckBoxTrueState(veifyCodeCheckbox)
                signUpBtn.isEnabled = true
            }
            else {
                changeCheckBoxFalseState(veifyCodeCheckbox)
                signUpBtn.isEnabled = false
                Toast.makeText(this, "Wrong Verification Code", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.startLoginActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                callLoginActivity()
            }
        })

        IDcheckbox.isEnabled = false
        PWcheckbox1.isEnabled = false
        PWcheckbox2.isEnabled = false
        emailCheckbox.isEnabled = false
        Recommendcheckbox.isChecked = true
        Recommendcheckbox.isEnabled = false
        veifyCodeCheckbox.isEnabled = false

        verifyBtn.isEnabled = false
        submitBtn.isEnabled = false
        signUpBtn.isEnabled = false
    }

}
