package com.rudder.data.remote


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.GetPostInfo
import com.rudder.data.PreviewPost
import com.rudder.data.Response
import retrofit2.http.*


interface PostService {
    @POST("/board/renderPost")
    suspend fun renderPost(
        @Body getPostInfo: GetPostInfo

    ) : ArrayList<PreviewPost>

    @POST("/board/addPost")
    suspend fun addPost(
        @Body addPostInfo: AddPostInfo
    ) : Response<AddPostResponse>

    @POST("/board/isLiked")
    suspend fun isLikePost(
        @Body isLikePostInfo: IsLikePostInfo
    ): Response<JsonObject>

    @POST("/board/addlike")
    suspend fun addLikePost(
        @Body addLikePostInfo: AddLikePostInfo
    ): Response<JsonObject>

    @POST("/board/addPostViewCount")
    suspend fun addPostViewCount(
        @Body addPostViewCountInfo: AddPostViewCountInfo
    ): Response<JsonObject>


    @POST("/board/postFromPostId")
    suspend fun postFromIdService(
        @Body postFromIdRequest: PostFromIdRequest
    ): Response<PostFromIdResponse>



    @GET("/posts")
    suspend fun getPostsService(
        @Header("Authorization") token : String,
        @Query("categoryId") categoryId: Int?,
        @Query("endPostId") endPostId : Int?,
        @Query("searchBody") searchBody : String?,
        ): retrofit2.Response<GetPostsResponse>


    @GET("/posts/{postId}")
    suspend fun getPostFromIdService(
        @Header("Authorization") token : String,
        @Path("postId") postId: Int?
    ): retrofit2.Response<PreviewPost>




}

data class GetPostsResponse(
    val posts : List<PreviewPost>
)


data class AddPostViewCountInfo(
    @SerializedName("post_id")
    val postId: Int
)

data class AddLikePostInfo(
    @SerializedName("post_id")
    val postId:Int,
    @SerializedName("token")
    val token:String,
    @SerializedName("plusValue")
    val plusValue:Int
)

data class IsLikePostInfo(
    @SerializedName("post_id")
    val postId:Int,
    @SerializedName("token")
    val token:String
)


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
        @SerializedName("category_id")
        val categoryId:Int
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
        val isSuccess:Boolean,
        @SerializedName("post_id")
        val postId:Int
)


data class PostFromIdResponse(
    val post : PreviewPost?,
    val isSuccess : Boolean,
    val error : ResponseEnum,
)


data class PostFromIdRequest(
    val postId:Int,
    val token:String
)