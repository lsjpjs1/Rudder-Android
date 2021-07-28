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
import com.rudder.data.LoginInfo
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUpViewModel  : ViewModel() {
    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    val _userPasswordCheck = MutableLiveData<String>()
    val _userRecommendCode = MutableLiveData<String>()
    val _userEmailID = MutableLiveData<String>()
    val _userEmailDomain = MutableLiveData<String>()
    val _userVerificationCode = MutableLiveData<String>()

    val _pwCheck = MutableLiveData<Boolean>()


    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode

    val pwCheck : LiveData<Boolean> = _pwCheck

    val passwordRg = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)

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

    fun callIdCheck(){}
//        GlobalScope.launch {
//            val result = repository.login(LoginInfo(userId.value!!,userPassword.value!!))
//            viewModelScope.launch{
//                if(result){
//                    _startMainActivity.value = Event(true)
//                }else{
//                    _showLoginErrorToast.value = Event(true)
//                }
//            }
//        }


    fun callPwCheck() {

    }

    fun callPwConfirm() {

    }


    fun callEmail(){

    }

    fun callVeificationCodeCheck(){

    }

}