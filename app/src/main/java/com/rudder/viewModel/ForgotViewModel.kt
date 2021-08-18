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

class ForgotViewModel() : ViewModel() {

    val _forgotEmail = MutableLiveData<String>()
    val _forgotverifyCode = MutableLiveData<String>()

    val forgotEmail: LiveData<String> = _forgotEmail
    val forgotverifyCode: LiveData<String> = _forgotverifyCode



    private val repository = Repository()

    init {
        _forgotEmail.value = ""
        _forgotverifyCode.value = ""
    }




}