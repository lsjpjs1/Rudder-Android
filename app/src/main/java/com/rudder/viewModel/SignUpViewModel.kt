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
import com.rudder.data.*
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
    val _idCheck = MutableLiveData<Boolean>()
    val _verifyCodeCheck = MutableLiveData<Boolean>()

    private val _startLoginActivity = MutableLiveData<Boolean>()

    val _schoolSelectNext = MutableLiveData<Boolean>()
    val _schoolSelectBack = MutableLiveData<Boolean>()
    val _createAccountNext = MutableLiveData<Boolean>()
    val _createAccountBack = MutableLiveData<Boolean>()
    val _profileSettingNext = MutableLiveData<Boolean>()
    val _profileSettingBack = MutableLiveData<Boolean>()

    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode

    val emailCheck : LiveData<Boolean> = _emailCheck
    val idCheck : LiveData<Boolean> = _idCheck
    val verifyCodeCheck : LiveData<Boolean> = _verifyCodeCheck

    val startLoginActivity: LiveData<Boolean> = _startLoginActivity

    val schoolSelectNext: LiveData<Boolean> = _schoolSelectNext
    val schoolSelectBack: LiveData<Boolean> = _schoolSelectBack
    val createAccountNext: LiveData<Boolean> = _createAccountNext
    val createAccountBack: LiveData<Boolean> = _createAccountBack
    val profileSettingNext : LiveData<Boolean> = _profileSettingNext
    val profileSettingBack : LiveData<Boolean> = _profileSettingBack

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
            val result = repository.signUpIdDuplicated(IdDuplicatedInfo(idInput))
            Log.d(ContentValues.TAG, "callIdCheck 결과 : ${result}")
            _idCheck.postValue(result)

        }
    }

    fun callSendVeriCode() {
        GlobalScope.launch {
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpSendVerifyCode(EmailInfo(emailInput))
            Log.d(ContentValues.TAG, "callSendVeriCode 결과 : ${result}")
            _emailCheck.postValue(result)
        }
    }

    fun callCheckVeriCode() {
        GlobalScope.launch {
            val verifyCodeInput = _userVerificationCode.value!!
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpCheckVerifyCode(CheckVerifyCodeInfo(emailInput, verifyCodeInput))
            Log.d(ContentValues.TAG, "callCheckVeriCode 결과 : ${result}")
            _verifyCodeCheck.postValue(result)

        }
    }

    fun callCreateAccount() {
        GlobalScope.launch {
            val idInput = _userId.value!!
            val passwordInput = _userPassword.value!!
            val result = repository.signUpCreateAccount(AccountInfo(idInput, passwordInput))
            Log.d(ContentValues.TAG, "callCreateAccount 결과 : ${result}")
            _startLoginActivity.postValue(result)
        }
    }

}
