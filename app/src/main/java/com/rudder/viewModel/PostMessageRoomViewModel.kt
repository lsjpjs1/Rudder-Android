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
                _messages.value = messagesByRoom
                if (messagesByRoom[0].isSender){
                    _targetUserInfoId.value = messagesByRoom[0].receiveUserInfoId
                } else {
                    _targetUserInfoId.value = messagesByRoom[0].sendUserInfoId
                }
            }
        }
    }

    fun setPostMessageRoomId(roomId : Int) {
        _postMessageRoomId.value = roomId
    }
}