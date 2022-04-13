package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.BuildConfig
import com.rudder.data.local.App
import com.rudder.data.remote.AddUserRequestRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactUsViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val repository = Repository()

    val _userContactUsBody = MutableLiveData<String>()
    private val _isStringBlank = MutableLiveData<Event<Boolean>>()
    private val _isContactUsSuccess = MutableLiveData<Event<Boolean>>()

    val isStringBlank : LiveData<Event<Boolean>> = _isStringBlank
    val isContactUsSuccess : LiveData<Event<Boolean>> = _isContactUsSuccess
    val userContactUsBody : LiveData<String> = _userContactUsBody


    init {
        _userContactUsBody.value = ""
    }


    fun submitUserRequest() {
        if ( _userContactUsBody.value!!.isBlank() ) {
            MainActivityViewModel._isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val addUserRequestRequest =
                    AddUserRequestRequest(App.prefs.getValue(tokenKey)!!, _userContactUsBody.value!!)
                val result = repository.addUserRequest(addUserRequestRequest)
                _isContactUsSuccess.postValue(Event(result))

                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("ContactUsViewModel","onCleared" )
    }

}