package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

// spring 완료
interface BlockUserService {
    @POST("/users/blockUser")
    suspend fun blockUser(
        @Body blockUserRequest: BlockUserRequest
    ) : Response<BlockUserResponse>
}

data class BlockUserRequest(
    val token: String,
    val blockUserInfoId: Int
)

data class BlockUserResponse(
    val isSuccess: Boolean
)