package com.rudder.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import retrofit2.http.Body
import retrofit2.http.POST

interface PostService {
    @POST("/board/renderPost")
    suspend fun renderPost(
        @Body board_type:String, @Body pagingIndex:Int, @Body endPostId:Int
    ) : ArrayList<Post>

}