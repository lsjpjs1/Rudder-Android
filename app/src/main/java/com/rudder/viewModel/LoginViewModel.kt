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

class LoginViewModel() : ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    private val _showLoginErrorToast = MutableLiveData<Event<Boolean>>()
    private val _startMainActivity = MutableLiveData<Event<Boolean>>()
    private val _startSignUpActivity = MutableLiveData<Event<Boolean>>()

    val showLoginErrorToast: LiveData<Event<Boolean>> = _showLoginErrorToast
    val startMainActivity: LiveData<Event<Boolean>> = _startMainActivity
    val startSignUpActivity: LiveData<Event<Boolean>> = _startSignUpActivity

    private val repository = Repository()
    init {
        userId.value = ""
        userPassword.value = ""
    }

    fun callSignUp(){
        _startSignUpActivity.value = Event(true)
    }

    fun callLogin(){
        GlobalScope.launch {
            val result = repository.login(LoginInfo(userId.value!!,userPassword.value!!))
            viewModelScope.launch{
                if(result){
                    _startMainActivity.value = Event(true)
                }else{
                    _showLoginErrorToast.value = Event(true)
                }
            }
        }
    }


}