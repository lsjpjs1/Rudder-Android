package com.rudder.viewModel

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.RequestCategoryInfo
import com.rudder.data.local.App
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.launch

class RequestCategoryViewModel : ViewModel() {

    private val tokenKey = BuildConfig.TOKEN_KEY
    val _requestCategoryNameBody = MutableLiveData<String>()
    val requestCategoryNameBody : LiveData<String> = _requestCategoryNameBody

    val _isStringBlank = MutableLiveData<Event<Boolean>>()
    val isStringBlank : LiveData<Event<Boolean>> = _isStringBlank

    val _isRequestCategorySuccess = MutableLiveData<Event<Boolean>>()
    val isRequestCategorySuccess : LiveData<Event<Boolean>> = _isRequestCategorySuccess


    init {
        _requestCategoryNameBody.value = ""
    }


    fun requestCategoryName(){
        if (_requestCategoryNameBody.value!!.isBlank()) {
            _isStringBlank.value = Event(true)
        } else {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            viewModelScope.launch {
                val requestCategoryApiResult = Repository().requestCategoryRepository(requestCategoryInfo = RequestCategoryInfo(App.prefs.getValue(tokenKey)!!, _requestCategoryNameBody.value!!, "-" ) )
                _isRequestCategorySuccess.postValue(Event(requestCategoryApiResult))
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }

}