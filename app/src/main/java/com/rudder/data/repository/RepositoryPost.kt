package com.rudder.data.repository

import com.rudder.data.GetPostInfo
import com.rudder.data.remote.GetPostResponse
import com.rudder.data.remote.PostApi

class RepositoryPost {

//    suspend fun noticeApiCall(noticeRequest: NoticeRequest) : Response<NoticeResponse> {
//        val apiResponse = NoticeApi.instance.callGetNoticeAPi(noticeRequest).await()
//        return apiResponse
//    }


    suspend fun getPostApiCall(getPostInfo: GetPostInfo) : retrofit2.Response<GetPostResponse> {
        val apiResponse = PostApi.instance.getPostsApi(getPostInfo).await()
        return apiResponse
    }

}