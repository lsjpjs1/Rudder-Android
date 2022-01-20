package com.rudder.viewModel

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

    val messages : LiveData<ArrayList<PostMessage>> = _messages

    fun getMessagesByRoom(postMessageRoomId : Int){
        GlobalScope.launch {
            val messagesByRoom = Repository().getMessagesByRoom(GetMessagesByRoomRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY!!)!!, postMessageRoomId))
            viewModelScope.launch {
                _messages.value = messagesByRoom
            }
        }
    }
}