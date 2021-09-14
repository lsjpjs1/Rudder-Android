package com.rudder.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyPageService {
    @POST("/signupin/profileImageUrl")
    suspend fun getMyProfileImageUrl(
        @Body myProfileImageRequest: MyProfileImageRequest
    ) : Response<MyProfileImageResponse>

    @POST("/users/addUserRequest")
    suspend fun addUserRequest(
        @Body addUserRequestRequest: AddUserRequestRequest
    ) : Response<JsonObject>
}

data class  MyProfileImageRequest(
    val token : String
)
data class MyProfileImageResponse(
    val url : String
)

data class AddUserRequestRequest(
    val token : String,
    @SerializedName("request_body")
    val requestBody : String
)