package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
    @POST("/message/sendPostMessage")
    suspend fun sendPostMessage(
        @Body sendPostMessageRequest: SendPostMessageRequest
    ) : Response<SendPostMessageResponse>
}

data class SendPostMessageRequest(
    val token: String,
    val receiveUserInfoId: Int,
    val messageBody: String
)

data class SendPostMessageResponse(
    val isSuccess: Boolean,
    val error: ResponseEnum
)