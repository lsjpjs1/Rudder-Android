package com.rudder.viewModel

import android.util.Log
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

class MyPageViewModel : ViewModel() {


    val _requestCategoryNameBody = MutableLiveData<String>()
    val requestCategoryNameBody : LiveData<String> = _requestCategoryNameBody

    val _isStringBlank = MutableLiveData<Event<Boolean>>()
    val isStringBlank : LiveData<Event<Boolean>> = _isStringBlank

    val _isRequestCategorySuccess = MutableLiveData<Event<Boolean>>()
    val isRequestCategorySuccess : LiveData<Event<Boolean>> = _isRequestCategorySuccess

    init {
        _requestCategoryNameBody.value = ""
    }



    private val tokenKey = BuildConfig.TOKEN_KEY


    fun requestCategoryName(){
        if (_requestCategoryNameBody.value!!.isBlank()) {
            _isStringBlank.value = Event(true)
        } else {
            viewModelScope.launch {
                ProgressBarUtil._progressBarFlag.postValue(Event(true))

                val requestCategoryApiResult = Repository().requestCategoryRepository(requestCategoryInfo = RequestCategoryInfo(App.prefs.getValue(tokenKey)!!, _requestCategoryNameBody.value!!, "-" ) )
                _isRequestCategorySuccess.postValue(Event(requestCategoryApiResult))

//                if (requestCategoryApiResult) { // result가 == true 이면
//                    _isRequestCategorySuccess.postValue(Event(requestCategoryApiResult))
//
//                } else { //result가 false 이면
//
//
//                }

                ProgressBarUtil._progressBarFlag.postValue(Event(false))


            }
        }


    }

}