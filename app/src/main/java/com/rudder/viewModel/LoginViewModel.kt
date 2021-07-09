package com.rudder.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.data.LoginInfo
import com.rudder.data.remote.LoginApi
import com.rudder.data.repository.Repository
import com.rudder.util.Navigator

class LoginViewModel(private val navigator: Navigator) : ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    private val repository = Repository()
    init {
        userId.value = ""
        userPassword.value = ""

    }

    fun callSignUp(){
        navigator.callSignUpActivity()
    }

    fun callLogin(){
        repository.login(LoginInfo(userId.value!!,userPassword.value!!))
    }


}