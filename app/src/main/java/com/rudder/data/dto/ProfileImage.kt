package com.rudder.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileImage(
    @SerializedName("_id")
    val profileImageId : Int,
    val hdLink : String,
    val previewLink : String
)

data class ProfileImageResponse(
    val profileImageList : ArrayList<ProfileImage>
)