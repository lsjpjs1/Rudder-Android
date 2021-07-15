package com.rudder.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostService {
    @Multipart
    @POST("/board/renderPost")
    suspend fun renderPost(
        @Part("board_type") board_type:String, @Part("pagingIndex") pagingIndex:Int, @Part("endPostId") endPostId:Int
    ) : ArrayList<Post>

}