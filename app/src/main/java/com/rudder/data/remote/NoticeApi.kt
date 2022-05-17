package com.rudder.data.remote

import com.rudder.BuildConfig
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
    private val noticeServiceSpring : NoticeService = RetrofitClientSpring.getClient(BuildConfig.BASE_URL_SPRING).create(NoticeService::class.java)



    fun getNotice(noticeRequest: NoticeRequest) : Deferred<Response<NoticeResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            noticeService.getNotice(noticeRequest)
        }
    }



    fun callGetNoticeAPi(noticeRequest: NoticeRequest) : Deferred<retrofit2.Response<NoticeResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            noticeServiceSpring.getNoticeService(noticeRequest.os, noticeRequest.version)
        }
    }

}
