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
        var likeCount : Int,
        @SerializedName(value = "status")
        val status : String,
        @SerializedName(value = "order_in_group")
        val orderInGroup : Int,
        @SerializedName(value = "group_num")
        val groupNum : Int,
        @SerializedName(value = "isMine")
        val isMine : Boolean,
        @SerializedName(value = "isLiked")
        var isLiked: Boolean,
        @SerializedName(value = "userProfileImageUrl")
        val userProfileImageUrl: String,
        @SerializedName(value = "user_info_id")
        val user_info_id: Int
)


