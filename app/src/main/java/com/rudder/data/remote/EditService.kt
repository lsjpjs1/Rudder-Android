package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.*
import retrofit2.http.Body
import retrofit2.http.POST

interface EditService {
    @POST("/board/editPost")
    suspend fun editPostService(
            @Body editPostInfo: EditPostInfo

    ) : Response<JsonObject>


    @POST("/comment/editComment")
    suspend fun editCommentService(
        @Body editCommentInfo: EditCommentInfo

    ) : Response<JsonObject>
}