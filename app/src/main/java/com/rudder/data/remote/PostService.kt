package com.rudder.data.remote


import com.google.gson.annotations.SerializedName
import com.rudder.data.GetPostInfo
import com.rudder.data.PreviewPost
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.sql.Timestamp


interface PostService {
    @POST("/board/renderPost")
    suspend fun renderPost(
        @Body getPostInfo: GetPostInfo

    ) : ArrayList<PreviewPost>

    @POST("/board/addPost")
    suspend fun addPost(
            @Body addPostInfo: AddPostInfo
    ) : Response<AddPostResponse>

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
        val imageInfoList:ArrayList<ImageInfo>,
        @SerializedName("category_name")
        val categoryName:String
)

data class ImageInfo(
        @SerializedName("file_link")
        val fileLink: String,
        @SerializedName("file_name")
        val fileName: String,
        @SerializedName("file_size")
        val fileSize: Int
)

data class AddPostResponse(
        val isSuccess:Boolean
)
