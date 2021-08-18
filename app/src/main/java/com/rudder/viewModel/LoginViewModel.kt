package com.rudder.viewModel



import android.util.Log
import android.widget.CompoundButton

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class LoginViewModel() : ViewModel() {
    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    private val _showLoginErrorToast = MutableLiveData<Event<Boolean>>()
    private val _startMainActivity = MutableLiveData<Event<Boolean>>()
    private val _startSignUpActivity = MutableLiveData<Event<Boolean>>()
    private val _startForgotActivity = MutableLiveData<Event<Boolean>>()

    val _autoLogin = MutableLiveData<Event<Boolean>>()

    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val showLoginErrorToast: LiveData<Event<Boolean>> = _showLoginErrorToast
    val startMainActivity: LiveData<Event<Boolean>> = _startMainActivity
    val startSignUpActivity: LiveData<Event<Boolean>> = _startSignUpActivity
    val startForgotActivity : LiveData<Event<Boolean>> = _startForgotActivity

    val autoLogin : LiveData<Event<Boolean>> = _autoLogin

    private val repository = Repository()

    init {
        _userId.value = ""
        _userPassword.value = ""
        _showLoginErrorToast.value = Event(false)
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (check) {
            _autoLogin.value = Event(true)
            prefs.setValue("autoLogin","true")
        } else{
            _autoLogin.value = Event(false)
            prefs.setValue("autoLogin","false")
        }
    }


    fun callSignUp(){
        _startSignUpActivity.value = Event(true)
    }

    fun callLogin(){
        GlobalScope.launch {
            val result = repository.login(LoginInfo(_userId.value!!,_userPassword.value!!))
            Log.d("result", "${result}" )
            viewModelScope.launch{
                if(result){
                    _startMainActivity.value = Event(true)
                }else{
                    _showLoginErrorToast.value = Event(true)
                }
            }
        }
    }


    fun callForgot(){
        _startForgotActivity.value = Event(true)
    }
}