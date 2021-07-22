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

        val checkBox1 = findViewById<CheckBox>(R.id.PWcheckbox1)
        val checkBox2 = findViewById<CheckBox>(R.id.PWcheckbox2)

        checkBox1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun
        }

        )

        myPWSecond.addTextChangedListener(object : TextWatcher {
            //입력이 끝났을 때
            //4. 비밀번호 일치하는지 확인
            override fun afterTextChanged(p0: Editable?) {
                if(myPWFirst.getText().toString().equals(myPWSecond.getText().toString())){

                    Toast.makeText(getApplicationContext(), "PW SAME",Toast.LENGTH_SHORT).show()
                    checkBox1.setChecked(true)

                    //pw_confirm.setText("비밀번호가 일치합니다.")
                    //pw_confirm.setTextColor(colorMain)
                    // 가입하기 버튼 활성화
                    //sign_up_btn2.isEnabled=true
                }
                else{
                    //pw_confirm.setText("비밀번호가 일치하지 않습니다.")
                    //pw_confirm.setTextColor(red)
                    // 가입하기 버튼 비활성화
                    //sign_up_btn2.isEnabled=false
                }
            }
            //입력하기 전
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            //텍스트 변화가 있을 시
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(myPWFirst.getText().toString().equals(myPWSecond.getText().toString())){
                    //pw_confirm.setText("비밀번호가 일치합니다.")
                    //pw_confirm.setTextColor(colorMain)
                    // 가입하기 버튼 활성화
                    //sign_up_btn2.isEnabled=true
                }
                else{
                    //pw_confirm.setText("비밀번호가 일치하지 않습니다.")
                    //pw_confirm.setTextColor(red)
                    // 가입하기 버튼 비활성화
                    //sign_up_btn2.isEnabled=false
                }
            }
        })

    }
}