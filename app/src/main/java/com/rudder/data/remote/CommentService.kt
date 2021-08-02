package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.GetCommentInfo
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentService {

    @POST("/board/showComment")
    suspend fun renderComment(
            @Body getCommentInfo: GetCommentInfo
    ) : JsonObject

}