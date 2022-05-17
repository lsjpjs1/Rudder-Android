package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoticeService {
    @POST("/signupin/getNotice")
    suspend fun getNotice(
        @Body noticeRequest: NoticeRequest
    ) : Response<NoticeResponse>


    @GET("/notice") // Spring Server
    suspend fun getNoticeService(
        @Query("os") os: String,
        @Query("version") version : String
    ) : retrofit2.Response<NoticeResponse>

}

data class NoticeRequest(
    val os: String,
    val version:String
)

data class NoticeResponse(
//    val isExist : Boolean,
//    val notice : String,
    val noticeBody : String
)