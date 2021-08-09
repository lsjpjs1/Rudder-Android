package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentService {

    @POST("/comment/showComment")
    suspend fun renderComment(
            @Body getCommentInfo: GetCommentInfo
    ) : Response<ArrayList<Comment>>

}