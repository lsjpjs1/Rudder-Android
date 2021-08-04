package com.rudder.viewModel

import android.content.ContentValues
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.data.*
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object SignUpViewModel  : ViewModel() {
    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    val _userPasswordCheck = MutableLiveData<String>()
    val _userRecommendCode = MutableLiveData<String>()
    val _userEmailID = MutableLiveData<String>()
    val _userEmailDomain = MutableLiveData<String>()
    val _userVerificationCode = MutableLiveData<String>()
    val _userSchool = MutableLiveData<Int>()

    val _emailCheck = MutableLiveData<Boolean>()
    val _idCheck = MutableLiveData<Boolean>()
    val _verifyCodeCheck = MutableLiveData<Boolean>()

    private val _startLoginActivity = MutableLiveData<Boolean>()

    val _schoolSelectNext = MutableLiveData<Event<Boolean>>()
    val _schoolSelectBack = MutableLiveData<Event<Boolean>>()
    val _createAccountNext = MutableLiveData<Event<Boolean>>()
    val _createAccountBack = MutableLiveData<Event<Boolean>>()
    val _profileSettingNext = MutableLiveData<Event<Boolean>>()
    val _profileSettingBack = MutableLiveData<Event<Boolean>>()

    val _passwordFlag = MutableLiveData<Event<Boolean>>()
    val _passwordCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailDomainFlag = MutableLiveData<Event<Boolean>>()
    val _schoolSelectFlag = MutableLiveData<Event<Boolean>>()


    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode
    val userSchool: LiveData<Int> = _userSchool


    val emailCheck: LiveData<Boolean> = _emailCheck
    val idCheck: LiveData<Boolean> = _idCheck
    val verifyCodeCheck: LiveData<Boolean> = _verifyCodeCheck

    val startLoginActivity: LiveData<Boolean> = _startLoginActivity

    val schoolSelectNext: LiveData<Event<Boolean>> = _schoolSelectNext
    val schoolSelectBack: LiveData<Event<Boolean>> = _schoolSelectBack
    val createAccountNext: LiveData<Event<Boolean>> = _createAccountNext
    val createAccountBack: LiveData<Event<Boolean>> = _createAccountBack
    val profileSettingNext: LiveData<Event<Boolean>> = _profileSettingNext
    val profileSettingBack: LiveData<Event<Boolean>> = _profileSettingBack

    val passwordFlag : LiveData<Event<Boolean>> = _passwordFlag
    val passwordCheckFlag : LiveData<Event<Boolean>> = _passwordCheckFlag
    val emailDomainFlag : LiveData<Event<Boolean>> = _emailDomainFlag
    val schoolSelectFlag : LiveData<Event<Boolean>> = _schoolSelectFlag

        private val repository = Repository()

    init {
        _userId.value = ""
        _userPassword.value = ""
        _userPasswordCheck.value = ""
        _userRecommendCode.value = ""
        _userEmailID.value = ""
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""

        _schoolSelectNext.value = Event(false)
        _schoolSelectBack.value = Event(false)
        _createAccountNext.value = Event(false)
        _createAccountBack.value = Event(false)
        _profileSettingNext.value = Event(false)
        _profileSettingBack.value = Event(false)
    }

    val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()
    val passwordRg =
        "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)


    fun clearValue() {
        _userId.value = ""
        _userPassword.value = ""
        _userPasswordCheck.value = ""
        _userRecommendCode.value = ""
        _userEmailID.value = ""
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""

    }

    fun onTextChangePW() {
        if (_userPassword.value!!.trim().matches(passwordRg) && _userPassword.value!!.isNotEmpty())
            _passwordFlag.value = Event(true)
        else
            _passwordFlag.value = Event(false)

        if (_userPassword.value!!.trim() == _userPasswordCheck.value!!.trim() && _userPassword.value!!.isNotEmpty())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangePWCheck() {
        if (_userPasswordCheck.value!!.trim() == _userPassword.value!!.trim() && _userPasswordCheck.value!!.isNotEmpty())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangeEmailDomain() {
        if (_userEmailDomain.value!!.trim().matches(emailRg))
            _emailDomainFlag.value = Event(true)
        else
            _emailDomainFlag.value = Event(false)
    }





    fun clickNextSchoolSelect(){
        _schoolSelectNext.value = Event(true)
    }

    fun clickBackSchoolSelect(){
        _schoolSelectBack.value = Event(true)
    }

    fun clickNextCreateAccount(){
        _createAccountNext.value = Event(true)
    }

    fun clickBackCreateAccount(){
        _createAccountBack.value = Event(true)
    }

    fun clickNextProfileSetting(){

    }

    fun clickBackProfileSetting(){
        _profileSettingBack.value = Event(true)
    }


    fun clickSchoolSelection()
    {
        if (_userSchool.value != 0) _schoolSelectFlag.value = Event(true)
        else _schoolSelectFlag.value = Event(false)
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
            _userVerificationCode.postValue("")
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
