package com.rudder.data.dto

import java.sql.Timestamp

data class JobsDetail(
    val jobTitle : String,

    val jobPostId : Int,
    val companyName : String,
    val jobType : String,
    val salary : String,
    val jobLocation : String,
    val jobPostURL : String,
    val postDate : Timestamp,
    val dueDate : Timestamp,
    val jobDescription : String,


    val isSaved : Boolean

)


