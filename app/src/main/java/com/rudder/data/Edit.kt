package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class EditPostInfo(
    @SerializedName(value = "post_body")
    val postBody: String,
    @SerializedName(value = "post_id")
    val postId: Int,
    @SerializedName(value = "token")
    val token: String
)


data class EditCommentInfo(
    @SerializedName(value = "comment_body")
    val commentBody: String,
    @SerializedName(value = "comment_id")
    val commentId: Int,
    @SerializedName(value = "token")
    val token: String
)
