package com.rudder.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.LoginInfo
import com.rudder.data.PreviewPost
import com.rudder.data.Response
import com.rudder.data.dto.ProfileImageResponse
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

    @POST("/board/requestJoinClub")
    suspend fun requestJoinClub(
        @Body requestJoinClubRequest: RequestJoinClubRequest
    ) : Response<RequestJoinClubResponse>

    @POST("/signupin/profileImageList")
    suspend fun getProfileImages() : Response<ProfileImageResponse>

    @POST("/board/myPosts")
    suspend fun getMyPosts(@Body myPostsRequest: MyPostsRequest) : Response<MyPostsResponse>
}

data class RequestJoinClubRequest(
    val token: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("request_body")
    val requestBody: String
)

data class RequestJoinClubResponse(
    val isSuccess: Boolean
)

data class  MyProfileImageRequest(
    val token : String
)

data class  MyPostsRequest(
    val token : String,
    val offset : Int
)
data class  MyPostsResponse(
    val isSuccess: Boolean,
    val posts: ArrayList<PreviewPost>,
    val error : ResponseEnum
)
data class MyProfileImageResponse(
    val url : String
)

data class AddUserRequestRequest(
    val token : String,
    @SerializedName("request_body")
    val requestBody : String
)