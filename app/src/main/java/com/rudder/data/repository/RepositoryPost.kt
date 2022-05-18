package com.rudder.data.repository

import com.rudder.data.GetPostInfo
import com.rudder.data.remote.GetPostsResponse
import com.rudder.data.remote.PostApi

class RepositoryPost {

//    suspend fun noticeApiCall(noticeRequest: NoticeRequest) : Response<NoticeResponse> {
//        val apiResponse = NoticeApi.instance.callGetNoticeAPi(noticeRequest).await()
//        return apiResponse
//    }


    suspend fun getPostApiCall(getPostInfo: GetPostInfo) : retrofit2.Response<GetPostsResponse> {
        val apiResponse = PostApi.instance.getPostsApi(getPostInfo).await()
        return apiResponse
    }

}