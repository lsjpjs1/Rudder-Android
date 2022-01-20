package com.rudder.data.remote

import com.rudder.data.Response
import com.rudder.data.dto.PostMessage
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
    @POST("/message/sendPostMessage")
    suspend fun sendPostMessage(
            @Body sendPostMessageRequest: SendPostMessageRequest
    ) : Response<SendPostMessageResponse>

    @POST("/message/getMessagesByRoom")
    suspend fun getMessagesByRoom(
            @Body getMessagesByRoomRequest: GetMessagesByRoomRequest
    ) : Response<GetMessagesByRoomResponse>
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

data class GetMessagesByRoomRequest(
        val token: String,
        val postMessageRoomId : Int
)

data class GetMessagesByRoomResponse(
        val isSuccess: Boolean,
        val error: String,
        val messages: ArrayList<PostMessage>
)