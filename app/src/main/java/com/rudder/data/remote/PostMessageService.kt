package com.rudder.data.remote

import com.rudder.data.Response
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import retrofit2.http.Body
import retrofit2.http.POST

interface PostMessageService {
    @POST("/message/getMyMessageRooms")
    suspend fun getMyMessageRooms(
            @Body getMyMessageRoomsRequest: GetMyMessageRoomsRequest
    ) : Response<GetMyMessageRoomsResponse>
}

data class GetMyMessageRoomsRequest(
    val token: String
)

data class GetMyMessageRoomsResponse(
    val isSuccess: Boolean,
    val error: String,
    val rooms: ArrayList<PostMessageRoom>
)