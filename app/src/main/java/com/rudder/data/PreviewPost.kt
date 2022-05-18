package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

//data class PreviewPost(
//    @SerializedName(value = "post_id")
//    val postId: Int,
//    @SerializedName(value = "user_id")
//    val userId: String,
//    @SerializedName(value = "user_info_id")
//    val userInfoId: Int,
//    @SerializedName(value = "post_body")
//    var postBody: String,
//    @SerializedName(value = "post_title")
//    val postTitle: String,
//    @SerializedName(value = "post_time")
//    val postTime: Timestamp,
//    @SerializedName(value = "comment_count")
//    var commentCount: Int,
//    @SerializedName(value = "like_count")
//    var likeCount: Int,
//    @SerializedName(value = "post_view")
//    val postView: Int,
//    @SerializedName(value = "category_name")
//    val categoryName: String,
//    @SerializedName(value = "category_id")
//    val categoryId: Int,
//    @SerializedName(value = "isLiked")
//    var isLiked: Boolean,
//    @SerializedName(value = "isMine")
//    val isMine: Boolean,
//    @SerializedName(value = "imageUrls")
//    val imageUrls: ArrayList<String>,
//    @SerializedName(value = "userProfileImageUrl")
//    val userProfileImageUrl: String,
//    @SerializedName(value = "category_abbreviation")
//    val categoryAbbreviation : String
//)



data class PreviewPost(
    val postId: Int,
    val userInfoId: Int,
    @SerializedName(value = "userNickname")
    val userId: String,
    var postBody: String,
    val postTime: Timestamp,
    var commentCount: Int,
    var likeCount: Int,
    val categoryName: String,
    val categoryId: Int,
    var isLiked: Boolean,
    val isMine: Boolean,
    val imageUrls: ArrayList<String>,
    val userProfileImageUrl: String,
    val categoryAbbreviation : String
)
