package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID = LiveData<String> = _userEmailID
    val userEmailDomain = LiveData<String> = _userEmailDomain
    val userVerificationCode = LiveData<String> = _userVerificationCode


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

    fun callIdCheck(){

    }

    fun callEmail(){

    }

    fun callVeificationCodeCheck(){

    }

}