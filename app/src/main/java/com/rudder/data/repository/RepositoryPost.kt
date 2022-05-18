package com.rudder.data.repository

import com.rudder.data.GetPostInfo
import com.rudder.data.PreviewPost
import com.rudder.data.remote.GetPostsResponse
import com.rudder.data.remote.PostApi
import com.rudder.data.remote.PostFromIdRequest

class RepositoryPost {

//    suspend fun noticeApiCall(noticeRequest: NoticeRequest) : Response<NoticeResponse> {
//        val apiResponse = NoticeApi.instance.callGetNoticeAPi(noticeRequest).await()
//        return apiResponse
//    }


    suspend fun getPostsApiCall(getPostInfo: GetPostInfo) : retrofit2.Response<GetPostsResponse> {
        val apiResponse = PostApi.instance.getPostsApi(getPostInfo).await()
        return apiResponse
    }



    suspend fun getPostFromIdApiCall(postFromIdRequest: PostFromIdRequest) : retrofit2.Response<PreviewPost> {
        val apiResponse = PostApi.instance.getPostFromIdApi(postFromIdRequest).await()
        return apiResponse
    }




}