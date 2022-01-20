package com.rudder.data.remote

import com.rudder.BuildConfig
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MessageApi {
    companion object{
        val instance = MessageApi()
    }

    private val messageService : MessageService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(MessageService::class.java)

    fun sendPostMessage(sendPostMessageRequest: SendPostMessageRequest) : Deferred<Response<SendPostMessageResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            messageService.sendPostMessage(sendPostMessageRequest)
        }
    }

    fun getMessagesByRoom(getMessagesByRoomRequest: GetMessagesByRoomRequest) : Deferred<Response<GetMessagesByRoomResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            messageService.getMessagesByRoom(getMessagesByRoomRequest)
        }
    }
}