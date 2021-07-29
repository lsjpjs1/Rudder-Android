package com.rudder.viewModel

import android.content.ContentValues
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.R
import com.rudder.data.EmailInfo
import com.rudder.data.LoginInfo
import com.rudder.data.remote.Emailaddress
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel  : ViewModel() {
    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    val _userPasswordCheck = MutableLiveData<String>()
    val _userRecommendCode = MutableLiveData<String>()
    val _userEmailID = MutableLiveData<String>()
    val _userEmailDomain = MutableLiveData<String>()
    val _userVerificationCode = MutableLiveData<String>()

    val _emailCheck = MutableLiveData<Boolean>()


    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode

    val emailCheck: LiveData<Boolean> = _emailCheck

    val passwordRg =
        "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)

    private val repository = Repository()

    init {
        _userId.value = ""
        _userPassword.value = ""
        _userPasswordCheck.value = ""
        _userRecommendCode.value = ""
        _userEmailID.value = ""
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""
    }

    fun callIdCheck() {
        GlobalScope.launch {
            val idInput = _userId.value!!

            val result = repository.signUpSendVerifyCode(I(emailInput))

        }

    fun callSendVeriCode() {
        GlobalScope.launch {
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpSendVerifyCode(EmailInfo(emailInput))
            Log.d(ContentValues.TAG, "결과 : ${result}")
            _emailCheck.postValue(result)
        }
    }


    fun callCheckVeriCode() {
        GlobalScope.launch {
            val verifyInput = _userVerificationCode.value!!
            //val result = repository.signUpSendVerifyCode(EmailInfo(emailInput))
            //Log.d(ContentValues.TAG, "결과 : ${result}")
            //_emailCheck.postValue(result)

            Log.d(ContentValues.TAG, "_emailCheck : ${_emailCheck.value}")

        }
    }

}
