package com.rudder.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.LoginInfo
import com.rudder.data.remote.LoginApi
import com.rudder.data.repository.Repository
import com.rudder.util.Navigator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val navigator: Navigator) : ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val test = MutableLiveData<String>()
    private val repository = Repository()
    init {
        userId.value = ""
        userPassword.value = ""
        test.value = ""
    }

    fun callSignUp(){
        navigator.callSignUpActivity()
    }

    fun callLogin(){
        GlobalScope.launch {
            val a = repository.login(LoginInfo(userId.value!!,userPassword.value!!)).await()
            viewModelScope.launch {
                test.value = a.toString()
            }

        }

    }


}