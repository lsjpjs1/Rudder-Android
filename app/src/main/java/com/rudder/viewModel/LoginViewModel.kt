package com.rudder.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.util.Navigator

class LoginViewModel(private val navigator: Navigator) : ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    init {
        userId.value = ""
        userPassword.value = ""

    }

    fun callSignUp(){
        navigator.callSignUpActivity()
    }






}