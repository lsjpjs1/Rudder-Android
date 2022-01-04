package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EditUserService {
    @POST("/users/updateNickname")
    suspend fun updateNickname(
        @Body updateNicknameRequest: UpdateNicknameRequest
    ) : Response<UpdateNicknameResponse>

}

data class UpdateNicknameRequest(
    val token: String,
    val nickname: String
)

data class UpdateNicknameResponse(
    val isSuccess: Boolean,
    val error: ResponseEnum
)