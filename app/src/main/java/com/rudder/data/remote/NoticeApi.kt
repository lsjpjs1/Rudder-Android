package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class NoticeApi {
    companion object{
        val instance = NoticeApi()
    }

    private val noticeService : NoticeService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(NoticeService::class.java)


    fun getNotice(noticeRequest: NoticeRequest) : Deferred<Response<NoticeResponse>> {

        return GlobalScope.async(Dispatchers.IO){
            noticeService.getNotice(noticeRequest)
        }

    }

}