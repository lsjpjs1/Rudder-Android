package com.rudder.data.repository

import com.rudder.data.remote.NoticeApi
import com.rudder.data.remote.NoticeRequest
import com.rudder.data.remote.NoticeResponse
import retrofit2.Response

class RepositoryNotice {

    suspend fun noticeApiCall(noticeRequest: NoticeRequest) : Response<NoticeResponse> {
        val apiResponse = NoticeApi.instance.callGetNoticeAPi(noticeRequest).await()
        return apiResponse
    }

}