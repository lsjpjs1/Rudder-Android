package com.rudder.data.dto

import java.sql.Timestamp


data class JobsInfo(
    val jobTitle : String,
    val jobPostId : Int,
    val companyName : String,
    val jobType : String,
    val salary : String?,
    val postDate : Timestamp,
    val companyImage : String?,
    val isSaved : Boolean
)


