package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class DeletePostInfo(
    @SerializedName(value = "post_id")
    val postId: Int
)


data class DeleteCommentInfo(
    @SerializedName(value = "comment_id")
    val commentId: Int,
    @SerializedName(value = "post_id")
    val postId: Int
)
