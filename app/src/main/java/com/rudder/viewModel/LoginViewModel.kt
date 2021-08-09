package com.rudder.viewModel


import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
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

    val _autoLogin = MutableLiveData<Event<Boolean>>()

    val showLoginErrorToast: LiveData<Event<Boolean>> = _showLoginErrorToast
    val startMainActivity: LiveData<Event<Boolean>> = _startMainActivity
    val startSignUpActivity: LiveData<Event<Boolean>> = _startSignUpActivity
    val autoLogin : LiveData<Event<Boolean>> = _autoLogin

    private val repository = Repository()

    init {
        userId.value = ""
        userPassword.value = ""
        _showLoginErrorToast.value = Event(false)
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (check) {
            Log.d("Z1D1", "onCheckedChange: $check")
            _autoLogin.value = Event(true)
            App.prefs.setValue("autoLogin","true")

        } else{
            Log.d("Z1D1", "onCheckedChange: $check")
            App.prefs.setValue("autoLogin","false")
        }

        Log.d("App.prefs", "${      App.prefs.getValue("autoLogin")          }" )
    }


    fun callSignUp(){
        _startSignUpActivity.value = Event(true)
    }

    fun callLogin(){
        GlobalScope.launch {
            val result = repository.login(LoginInfo(userId.value!!,userPassword.value!!))
            Log.d("result", "${result }" )
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