package com.rudder.data.dto

import java.sql.Timestamp

data class JobsDetail(
    val jobTitle : String?,
    val jobId : Int?,
    val companyName : String?,
    val jobType : String?,
    val salary : String?,
    val location : String?,
    val jobUrl : String?,
    val uploadDate : Timestamp,
    val expireDate : String?,
    val jobDescription : String?,
    val isFavorite : Boolean

)


