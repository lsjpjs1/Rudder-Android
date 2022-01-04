package com.rudder.data.remote

import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EditUserService {
    @POST("/users/updateNickname")
    suspend fun updateNickname(
        @Body updateNicknameRequest: UpdateNicknameRequest
    ) : Response<UpdateResponse>

    @POST("/users/updateUserProfileImage")
    suspend fun updateProfileImage(
        @Body updateProfileImageRequest: UpdateProfileImageRequest
    ) : Response<UpdateResponse>

}
data class UpdateProfileImageRequest(
    val token: String,
    val profileImageId: Int
)
data class UpdateNicknameRequest(
    val token: String,
    val nickname: String
)

data class UpdateResponse(
    val isSuccess: Boolean,
    val error: ResponseEnum
)