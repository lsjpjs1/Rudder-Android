package com.rudder.data.dto

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp


data class JobsInfo(
    val jobTitle : String,
    val jobPostId : Int,
    val companyName : String,
    val jobType : String,
    val salary : String?,
    @SerializedName("uploadDate")
    val postDate : Timestamp,
    val companyImage : String?,
    @SerializedName("isFavorite")
    var isSaved : Boolean
)


