package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.data.local.App
import com.rudder.data.remote.GetMyMessageRoomsRequest
import com.rudder.data.repository.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostMessageViewModel: ViewModel() {
    /// 21 01 15 real final

    private val _myMessageRooms = MutableLiveData<ArrayList<PostMessageRoom>>()

    val myMessageRooms : LiveData<ArrayList<PostMessageRoom>> = _myMessageRooms

    fun getPostMessages() {

        GlobalScope.launch {
            val myMessageRooms = Repository().getMyMessages(GetMyMessageRoomsRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY!!)!!))
            viewModelScope.launch {
                _myMessageRooms.value = myMessageRooms
            }
        }

    }

}