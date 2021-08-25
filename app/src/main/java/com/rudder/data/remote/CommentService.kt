package com.rudder.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.sql.Timestamp

interface CommentService {

    @POST("/comment/showComment")
    suspend fun renderComment(
            @Body getCommentInfo: GetCommentInfo
    ) : Response<ArrayList<Comment>>

    @POST("/comment/addComment")
    suspend fun addComment(
        @Body addCommentInfo: AddCommentInfo
    ) : Response<JsonObject>

    @POST("/comment/addlike")
    suspend fun addLikeComment(
        @Body addLikeCommentInfo: AddLikeCommentInfo
    ): Response<JsonObject>
}

data class AddCommentInfo(
    @SerializedName(value = "post_id")
    val postId : Int,
    @SerializedName(value = "comment_body")
    val commentBody : String,
    @SerializedName(value = "token")
    val token : String,
    @SerializedName(value = "status")
    val status : String,
    @SerializedName(value = "group_num")
    val groupNum : Int
)

data class AddLikeCommentInfo(
    @SerializedName("comment_id")
    val commentId:Int,
    @SerializedName("token")
    val token:String,
    @SerializedName("plusValue")
    val plusValue:Int
)