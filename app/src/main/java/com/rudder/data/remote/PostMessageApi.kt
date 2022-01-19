package com.rudder.data.remote

import com.rudder.BuildConfig
import com.rudder.data.Response
import kotlinx.coroutines.*

class PostMessageApi {

    companion object{
        val instance = PostMessageApi()
    }

    private val postMessageService : PostMessageService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(PostMessageService::class.java)


    fun getMyMessageRooms(getMyMessageRoomsRequest: GetMyMessageRoomsRequest) : Deferred<Response<GetMyMessageRoomsResponse>> {
        return GlobalScope.async(Dispatchers.IO){
                postMessageService.getMyMessageRooms(getMyMessageRoomsRequest)
        }
    }
}