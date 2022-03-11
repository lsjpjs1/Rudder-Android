package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.local.App
import com.rudder.data.remote.ResponseEnum
import com.rudder.data.remote.SendPostMessageRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SendPostMessageDialogViewModel : ViewModel() {

    val _postMessageBody = MutableLiveData<String>()
    private val _messageReceiveUserInfoId = MutableLiveData<Int?>()
    private val _toastMessage = MutableLiveData<String?>()
    private val _closeFlag = MutableLiveData<Event<Boolean>>()
    val closeFlag : LiveData<Event<Boolean>> = _closeFlag
    val toastMessage : LiveData<String?> = _toastMessage


    init {
        _postMessageBody.value = ""
    }


    fun sendPostMessage() {
        if ( _postMessageBody.value!!.isBlank() ) {
            _toastMessage.value = "One or more fields is blank!"
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val token = App.prefs.getValue(BuildConfig.TOKEN_KEY)
                _messageReceiveUserInfoId.value?.let {
                    val result = Repository().sendPostMessage(SendPostMessageRequest(token!!,_messageReceiveUserInfoId.value!!,_postMessageBody.value!!))
                    viewModelScope.launch {
                        when(result.error){
                            ResponseEnum.SUCCESS -> {
                                _toastMessage.value = "Sending message complete!"
                                _closeFlag.value = Event(true)
                            }
                            ResponseEnum.UNKNOWN -> {
                                _toastMessage.value = "Sending message failed"
                            }
                            ResponseEnum.DATABASE -> {
                                _toastMessage.value = "Database failure"
                            }
                        }
                    }
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }

    fun setMessageReceiveUserInfoId(receiveUserINfoId : Int?) {
        _messageReceiveUserInfoId.value = receiveUserINfoId
    }
}