package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.DeleteCommentInfo
import com.rudder.data.DeletePostInfo
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DeletePostService {
    @POST("/board/deletePost")
    suspend fun deletePostService(
            @Body deletePostInfo: DeletePostInfo

    ) : Response<JsonObject>
}



interface DeleteCommentService {
    @POST("/comment/deleteComment")
    suspend fun deleteCommentService(
        @Body deleteCommentInfo: DeleteCommentInfo

    ) : Response<JsonObject>
}






