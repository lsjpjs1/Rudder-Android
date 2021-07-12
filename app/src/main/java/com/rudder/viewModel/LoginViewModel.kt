package com.rudder.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.data.LoginInfo
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
            val result = repository.login(LoginInfo(userId.value!!,userPassword.value!!))
            if(result){
                navigator.callMainActivity()
            }else{

            }
        }
    }


}