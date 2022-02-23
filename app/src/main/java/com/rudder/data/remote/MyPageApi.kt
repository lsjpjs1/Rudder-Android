package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import com.rudder.data.dto.ProfileImage
import com.rudder.data.dto.ProfileImageResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MyPageApi {

    companion object{
        val instance = MyPageApi()
    }

    private val myPageService : MyPageService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(MyPageService::class.java)


    fun getMyProfileImageUrl(myProfileImageRequest: MyProfileImageRequest) : Deferred<Response<MyProfileImageResponse>> {

        return GlobalScope.async(Dispatchers.IO){
            myPageService.getMyProfileImageUrl(myProfileImageRequest)
        }

    }

    fun addUserRequest(userRequestRequest: AddUserRequestRequest) : Deferred<Response<JsonObject>> {

        return GlobalScope.async(Dispatchers.IO){
            myPageService.addUserRequest(userRequestRequest)
        }

    }

    fun requestJoinClub(requestJoinClubRequest: RequestJoinClubRequest) : Deferred<Response<RequestJoinClubResponse>> {

        return GlobalScope.async(Dispatchers.IO){
            myPageService.requestJoinClub(requestJoinClubRequest)
        }

    }

    fun getProfileImages() : Deferred<Response<ProfileImageResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            myPageService.getProfileImages()
        }
    }

    fun getMyPosts(myPostsRequest: MyPostsRequest) : Deferred<Response<MyPostsResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            myPageService.getMyPosts(myPostsRequest)
        }
    }

    fun getPostsWithMyComment(postsWithMyCommentRequest: PostsWithMyCommentRequest) : Deferred<Response<PostsWithMyCommentResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            myPageService.getPostsWithMyComment(postsWithMyCommentRequest)
        }
    }

    fun logout(logoutRequest: LogoutRequest) : Deferred<Response<LogoutResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            myPageService.logout(logoutRequest)
        }
    }
}