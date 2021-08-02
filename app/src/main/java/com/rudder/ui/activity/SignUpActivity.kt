package com.rudder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.viewModel.SignUpViewModel
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

    fun buttonEnable(b0 : Boolean, b1 : Boolean, b2 : Boolean, b3 : Boolean, b4 : Boolean, inputButton : Button){
        inputButton.isEnabled = b0 && b1 && b2 && b3 && b4
    }


    fun callLoginActivity() {
        val intent = Intent(this, SchoolSelectionActivity::class.java)
        startActivity(intent)
    }

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

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this



        viewModel.userPassword.observe(this, Observer {
            if (it.trim().matches(passwordRg) && it.isNotEmpty()) changeCheckBoxTrueState(
                PWcheckbox1
            )
            else {
                changeCheckBoxFalseState(PWcheckbox1)
                Toast.makeText(this, "비밀번호는 숫자,문자,특수문자 중 2가지 포함(8~15자)", Toast.LENGTH_SHORT).show()
            }
            if (it.trim() == viewModel.userPasswordCheck.value!!.trim() && it.isNotEmpty()) {
                changeCheckBoxTrueState(PWcheckbox2)
            } else {
                changeCheckBoxFalseState(PWcheckbox2)
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, signUpBtn)
        })

        viewModel.userPasswordCheck.observe(this, Observer {
            if (it.trim() == viewModel.userPassword.value!!.trim() && it.isNotEmpty()) {
                changeCheckBoxTrueState(PWcheckbox2)
            } else {
                changeCheckBoxFalseState(PWcheckbox2)
                Toast.makeText(this, "Please Check, Password and Password Confirm",Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, signUpBtn)
        })

        viewModel.userEmailDomain.observe(this, Observer {
            if (it.trim().matches(emailRg)) {
                changeCheckBoxTrueState(emailCheckbox)
            } else {
                changeCheckBoxFalseState(emailCheckbox)
                Toast.makeText(this, "Please Check, Right Email Domain", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, signUpBtn)
        })

        viewModel.idCheck.observe(this, Observer {
            if (!it) changeCheckBoxTrueState(IDcheckbox)
            else {
                changeCheckBoxFalseState(IDcheckbox)
                Toast.makeText(this, "ID is duplicated", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, signUpBtn)
        })

        viewModel.emailCheck.observe(this, Observer {
            if (it) submitBtn.isEnabled = true
            else {
                submitBtn.isEnabled = false
                Toast.makeText(this, "Email must be Naver Email", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, signUpBtn)
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
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, signUpBtn)
        })

        viewModel.startLoginActivity.observe(this, Observer {
            if (it) callLoginActivity()
            Toast.makeText(this, "Sign Up Successfully, Complete!", Toast.LENGTH_SHORT).show()
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
