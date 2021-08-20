package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class PreviewPost(
    @SerializedName(value = "post_id")
    val postId: Int,
    @SerializedName(value = "user_id")
    val userId: String,
    @SerializedName(value = "post_body")
    var postBody: String,
    @SerializedName(value = "post_title")
    val postTitle: String,
    @SerializedName(value = "post_time")
    val postTime: Timestamp,
    @SerializedName(value = "comment_count")
    val commentCount: Int,
    @SerializedName(value = "like_count")
    val likeCount: Int,
    @SerializedName(value = "post_view")
    val postView: Int,
    @SerializedName(value = "category_name")
    val categoryName: String,
    @SerializedName(value = "category_id")
    val categoryId: Int
)
