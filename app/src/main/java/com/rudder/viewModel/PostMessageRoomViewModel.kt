package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.dto.PostMessage
import com.rudder.data.local.App
import com.rudder.data.remote.GetMessagesByRoomRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostMessageRoomViewModel : ViewModel() {

    private val _messages = MutableLiveData<ArrayList<PostMessage>>()
    private val _targetUserInfoId = MutableLiveData<Int?>()
    private val _postMessageRoomId = MutableLiveData<Int>()

    val postMessageRoomId = _postMessageRoomId
    val targetUserInfoId : LiveData<Int?> = _targetUserInfoId
    val messages : LiveData<ArrayList<PostMessage>> = _messages


    fun getMessagesByRoom(){
        GlobalScope.launch {
            val messagesByRoom = Repository().getMessagesByRoom(GetMessagesByRoomRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY!!)!!, _postMessageRoomId.value!!))
            viewModelScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                _messages.value = messagesByRoom
                if (messagesByRoom.size>0){
                    if (messagesByRoom[0].isSender){
                        _targetUserInfoId.value = messagesByRoom[0].receiveUserInfoId
                    } else {
                        _targetUserInfoId.value = messagesByRoom[0].sendUserInfoId
                    }
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }


    fun setPostMessageRoomId(roomId : Int) {
        _postMessageRoomId.value = roomId
    }
}