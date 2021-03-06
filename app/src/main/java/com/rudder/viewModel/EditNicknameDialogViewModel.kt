package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.local.App
import com.rudder.data.remote.ResponseEnum
import com.rudder.data.remote.UpdateNicknameRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditNicknameDialogViewModel : ViewModel() {
    val _newNickname = MutableLiveData<String?>()
    private val _toastMessage = MutableLiveData<String?>()
    private val _closeFlag = MutableLiveData<Event<Boolean>>()

    val closeFlag : LiveData<Event<Boolean>> = _closeFlag
    val toastMessage : LiveData<String?> = _toastMessage

    init {
        _newNickname.value = ""
    }


    fun updateNickname(){
        if ( _newNickname.value!!.isBlank() ) {
            _toastMessage.value = "One or more fields is blank!"
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                val result=Repository().updateNickname(UpdateNicknameRequest(App.prefs.getValue("token")!!,_newNickname.value!!))
                viewModelScope.launch {
                    when(result.error){
                        ResponseEnum.SUCCESS -> {
                            _toastMessage.value = "Successfully changed!"
                            _closeFlag.value = Event(true)
                        }
                        ResponseEnum.UNKNOWN -> {
                            _toastMessage.value = "Editing failed"
                        }
                        ResponseEnum.DATABASE -> {
                            _toastMessage.value = "Database failure"
                        }
                        ResponseEnum.DUPLICATE -> {
                            _toastMessage.value = "This nickname already exists. Please try other nickname."
                        }
                    }
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }

        }

    }


}