package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoticeService {
    @POST("/signupin/getNotice")
    suspend fun getNotice(
        @Body noticeRequest: NoticeRequest
    ) : Response<NoticeResponse>
}

data class NoticeRequest(
    val os: String,
    val version:String
)

data class NoticeResponse(
    val isExist:Boolean,
    val notice:String
)