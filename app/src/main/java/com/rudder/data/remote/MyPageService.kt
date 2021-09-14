package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyPageService {
    @POST("/signupin/profileImageUrl")
    suspend fun getMyProfileImageUrl(
        @Body myProfileImageRequest: MyProfileImageRequest
    ) : Response<MyProfileImageResponse>
}

data class  MyProfileImageRequest(
    val token : String
)
data class MyProfileImageResponse(
    val url : String
)