package com.rudder.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.viewModel.SignUpViewModel
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rudder.databinding.ActivitySignUpBinding
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*


class CreateAccountFragment : Fragment() {

    private val viewModel = SignUpViewModel
    private lateinit var createAccount : FragmentCreateAccountBinding

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


//    fun callLoginActivity() {
//        val intent = Intent(this, SchoolSelectionActivity::class.java)
//        startActivity(intent)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createAccount = DataBindingUtil.inflate<FragmentCreateAccountBinding>(inflater,R.layout.fragment_create_account,container,false)
        createAccount.signUpVM = viewModel


        viewModel.userPassword.observe(viewLifecycleOwner, Observer {
            if (it.trim().matches(passwordRg) && it.isNotEmpty()) changeCheckBoxTrueState(
                PWcheckbox1
            )
            else {
                changeCheckBoxFalseState(PWcheckbox1)
                Toast.makeText(activity, "비밀번호는 숫자,문자,특수문자 중 2가지 포함(8~15자)", Toast.LENGTH_SHORT).show()
            }
            if (it.trim() == viewModel.userPasswordCheck.value!!.trim() && it.isNotEmpty()) {
                changeCheckBoxTrueState(PWcheckbox2)
            } else {
                changeCheckBoxFalseState(PWcheckbox2)
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

        viewModel.userPasswordCheck.observe(viewLifecycleOwner, Observer {
            if (it.trim() == viewModel.userPassword.value!!.trim() && it.isNotEmpty()) {
                changeCheckBoxTrueState(PWcheckbox2)
            } else {
                changeCheckBoxFalseState(PWcheckbox2)
                Toast.makeText(activity, "Please Check, Password and Password Confirm",Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

        viewModel.userEmailDomain.observe(viewLifecycleOwner, Observer {
            if (it.trim().matches(emailRg)) {
                changeCheckBoxTrueState(emailCheckbox)
            } else {
                changeCheckBoxFalseState(emailCheckbox)
                Toast.makeText(activity, "Please Check, Right Email Domain", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

        viewModel.idCheck.observe(viewLifecycleOwner, Observer {
            if (!it) changeCheckBoxTrueState(IDcheckbox)
            else {
                changeCheckBoxFalseState(IDcheckbox)
                Toast.makeText(activity, "ID is duplicated", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

        viewModel.emailCheck.observe(viewLifecycleOwner, Observer {
            if (it) submitBtn.isEnabled = true
            else {
                submitBtn.isEnabled = false
                Toast.makeText(activity, "Email must be Naver Email", Toast.LENGTH_SHORT).show()
            }
            changeCheckBoxFalseState(veifyCodeCheckbox)
            verifyCode.text.clear()
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, true, verifyBtn)
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

        viewModel.verifyCodeCheck.observe(viewLifecycleOwner, Observer {
            if (it) {
                changeCheckBoxTrueState(veifyCodeCheckbox)
                createAccountNextBtn.isEnabled = true
            }
            else {
                changeCheckBoxFalseState(veifyCodeCheckbox)
                createAccountNextBtn.isEnabled = false
                Toast.makeText(activity, "Wrong Verification Code", Toast.LENGTH_SHORT).show()
            }
            buttonEnable(IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked, createAccountNextBtn)
        })

//        viewModel.startLoginActivity.observe(viewLifecycleOwner, Observer {
//            if (it) callLoginActivity()
//            Toast.makeText(this, "Create account Successfully, Complete!", Toast.LENGTH_SHORT).show()
//        })


        createAccount.root.IDcheckbox.isEnabled = false
        createAccount.root.PWcheckbox1.isEnabled = false
        createAccount.root.PWcheckbox2.isEnabled = false
        createAccount.root.emailCheckbox.isEnabled = false
        createAccount.root.Recommendcheckbox.isChecked = true
        createAccount.root.Recommendcheckbox.isEnabled = false
        createAccount.root.veifyCodeCheckbox.isEnabled = false

        createAccount.root.verifyBtn.isEnabled = false
        createAccount.root.submitBtn.isEnabled = false
        createAccount.root.createAccountNextBtn.isEnabled = false


        return createAccount.root
    }
}