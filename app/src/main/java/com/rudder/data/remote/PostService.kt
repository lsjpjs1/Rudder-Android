package com.rudder.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.GetPostInfo
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostService {
    @POST("/board/renderPost")
    suspend fun renderPost(
        @Body getPostInfo: GetPostInfo
    ) : ArrayList<Post>

    @POST("/board/addPost")
    suspend fun addPost(
            @Body addPostInfo: AddPostInfo
    ) : JsonObject
}

data class AddPostInfo(
        @SerializedName("board_type")
        val boardType:String,
        @SerializedName("post_title")
        val postTitle:String,
        @SerializedName("post_body")
        val postBody:String,
        @SerializedName("token")
        val token:String,
        @SerializedName("imageInfoList")
        val imageInfoList:ArrayList<ImageInfo>
)

data class ImageInfo(
        @SerializedName("file_link")
        val fileLink: String,
        @SerializedName("file_name")
        val fileName: String,
        @SerializedName("file_size")
        val fileSize: Int
)