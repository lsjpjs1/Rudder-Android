package com.rudder.viewModel

import android.content.ContentValues
import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.CheckVerifyCodeInfo
import com.rudder.data.EmailInfo
import com.rudder.data.IdDuplicatedInfo
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForgotViewModel() : ViewModel() {

    val _forgotEmail = MutableLiveData<String>()
    val _forgotverifyCode = MutableLiveData<String>()

    val _findIDClick = MutableLiveData<Event<Boolean>>()
    val _findPasswordClick = MutableLiveData<Event<Boolean>>()
    val _emailCheckFlag = MutableLiveData<Event<Boolean>>()
    val _verifyCodeCheckFlag = MutableLiveData<Event<Boolean>>()


    val forgotEmail: LiveData<String> = _forgotEmail
    val forgotverifyCode: LiveData<String> = _forgotverifyCode

    val findPasswordClick : LiveData<Event<Boolean>> = _findPasswordClick
    val findIDClick : LiveData<Event<Boolean>> = _findIDClick
    val emailCheckFlag : LiveData<Event<Boolean>> = _emailCheckFlag
    val verifyCodeCheckFlag: LiveData<Event<Boolean>> = _verifyCodeCheckFlag


    private val repository = Repository()

    init {
        _forgotEmail.value = ""
        _forgotverifyCode.value = ""
        _findIDClick.value = Event(false)
        _findPasswordClick.value = Event(false)
    }

    val emailRg = android.util.Patterns.EMAIL_ADDRESS.toRegex()


    fun clickID(){
        _findIDClick.value = Event(true)
    }

    fun clickPassword(){
        _findPasswordClick.value = Event(true)
    }

    fun callCheckEmail() { // FIND ID Email Check
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val result = repository.findAccountID(EmailInfo(_forgotEmail.value!!))
            _emailCheckFlag.postValue(Event(result && _forgotEmail.value!!.matches(emailRg)))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }

    fun callSendVeriCode() { // Find Password Email Check
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val result = repository.findAccountPassword(EmailInfo(_forgotEmail.value!!))
            _emailCheckFlag.postValue(Event(result && _forgotEmail.value!!.matches(emailRg)))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }

    fun callSendPassword() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val result = repository.sendAccountPassword(CheckVerifyCodeInfo(_forgotEmail.value!!, _forgotverifyCode.value!!))
            _verifyCodeCheckFlag.postValue(Event(result))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }



}