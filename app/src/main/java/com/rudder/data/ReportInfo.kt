package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class ReportInfo(
    @SerializedName(value = "token")
    val token: String,
    @SerializedName(value = "post_id") // 일단, commentID 도 받게 함.
    val reportId: Int,
    @SerializedName(value = "report_body")
    val reportBody: String,
    @SerializedName(value = "post_type")
    val postType: String
)


