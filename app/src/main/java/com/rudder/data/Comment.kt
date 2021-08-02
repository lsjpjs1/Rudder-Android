package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Comment(
        @SerializedName(value = "user_id")
        val userId : String,
        @SerializedName(value = "comment_id")
        val commentId : Int,
        @SerializedName(value = "comment_body")
        val commentBody : String,
        @SerializedName(value = "post_time")
        val postTime : Timestamp,
        @SerializedName(value = "like_count")
        val likeCount : Int,
        @SerializedName(value = "isMine")
        val isMine : Boolean
)
