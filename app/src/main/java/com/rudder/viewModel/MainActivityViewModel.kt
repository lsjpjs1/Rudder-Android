package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rudder.util.Event

class MainActivityViewModel {
    companion object{

        val _isStringBlank = MutableLiveData<Event<Boolean>>()


        val isStringBlank : LiveData<Event<Boolean>> = _isStringBlank
    }

}