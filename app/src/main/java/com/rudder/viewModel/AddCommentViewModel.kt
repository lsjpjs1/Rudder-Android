package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.LoginInfo
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddCommentViewModel () : ViewModel() {
    val _commentBody = MutableLiveData<String>()

    override fun onCleared() {
        clearAllValues()
        super.onCleared()
    }
    private val repository = Repository()
    init {
        clearAllValues()
    }

    private fun clearAllValues(){
        _commentBody.value=""
    }



}