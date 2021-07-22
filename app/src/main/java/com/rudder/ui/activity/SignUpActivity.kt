package com.rudder.ui.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.viewModel.SignUpViewModel
import android.view.View
import android.widget.EditText
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    private val viewModel: SignUpViewModel = SignUpViewModel()


    val isExistBlank = false
    val isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
//        binding.signUpVM = viewModel
//        binding.lifecycleOwner = this
//        viewModel.userPassword.observe(this, Observer {
//            if(it==viewModel.userPasswordCheck.value && it.isNotBlank()){
//                Toast.makeText(this,"same",Toast.LENGTH_SHORT).show()
//            }
//        })
//        viewModel.userPasswordCheck.observe(this, Observer {
//            if(it==viewModel.userPassword.value && it.isNotBlank()){
//                Toast.makeText(this,"same",Toast.LENGTH_SHORT).show()
//            }
//        })
        setContentView(R.layout.activity_sign_up)

        var id = findViewById<EditText>(R.id.editTextTextPersonName1)
        //editTextTextPersonName1
        var myPWFirst = findViewById<EditText>(R.id.editTextTextPersonName4)
        var myPWSecond = findViewById<EditText>(R.id.editTextTextPersonName3)

        var emailDomain = findViewById<EditText>(R.id.editTextTextPersonName10)
        val emailInput: String

        val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()


        val checkBoxId = findViewById<CheckBox>(R.id.IDcheckbox)
        val checkBoxReco = findViewById<CheckBox>(R.id.Recommendcheckbox)
        val checkBox1 = findViewById<CheckBox>(R.id.PWcheckbox1)
        val checkBox2 = findViewById<CheckBox>(R.id.PWcheckbox2)
        val checkBoxEmail = findViewById<CheckBox>(R.id.emailCheckbox)

        checkBox1.setEnabled(false)
        checkBox2.setEnabled(false)

        checkBoxId.setEnabled(false)
        checkBoxReco.setEnabled(false)

        myPWSecond.addTextChangedListener(object : TextWatcher {
            //입력이 끝났을 때
            //4. 비밀번호 일치하는지 확인
            override fun afterTextChanged(p0: Editable?) {
                if (myPWFirst.getText().toString().equals(myPWSecond.getText().toString())) {

                    checkBox1.setEnabled(true)
                    checkBox1.setChecked(true)
                    checkBox1.setEnabled(false)

                    checkBox2.setEnabled(true)
                    checkBox2.setChecked(true)
                    checkBox2.setEnabled(false)
                } else {
                    checkBox1.setEnabled(true)
                    checkBox1.setChecked(false)
                    checkBox1.setEnabled(false)

                    checkBox2.setEnabled(true)
                    checkBox2.setChecked(false)
                    checkBox2.setEnabled(false)
                }
            }

            //입력하기 전
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            //텍스트 변화가 있을 시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (myPWFirst.getText().toString().equals(myPWSecond.getText().toString())) {
                    checkBox1.setEnabled(true)
                    checkBox1.setChecked(true)
                    checkBox1.setEnabled(false)

                    checkBox2.setEnabled(true)
                    checkBox2.setChecked(true)
                    checkBox2.setEnabled(false)
                } else {
                    checkBox1.setEnabled(true)
                    checkBox1.setChecked(false)
                    checkBox1.setEnabled(false)

                    checkBox2.setEnabled(true)
                    checkBox2.setChecked(false)
                    checkBox2.setEnabled(false)
                }
            }
        })



        emailDomain.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                if (emailDomain.getText().toString().trim().matches(emailRg)) {
                    checkBoxEmail.setEnabled(true)
                    checkBoxEmail.setChecked(true)
                    checkBoxEmail.setEnabled(false)
                } else {
                    checkBoxEmail.setEnabled(true)
                    checkBoxEmail.setChecked(false)
                    checkBoxEmail.setEnabled(false)
                }
            }

            //입력하기 전
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            //텍스트 변화가 있을 시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (emailDomain.getText().toString().trim().matches(emailRg)) {
                    checkBoxEmail.setEnabled(true)
                    checkBoxEmail.setChecked(true)
                    checkBoxEmail.setEnabled(false)
                } else {
                    checkBoxEmail.setEnabled(true)
                    checkBoxEmail.setChecked(false)
                    checkBoxEmail.setEnabled(false)
                }
            }
        })

    }
}
