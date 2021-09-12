package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CategorySelectInfo(
    @SerializedName(value = "token")
    val token: String,
    @SerializedName(value = "categoryIdList")
    val categoryIdList: ArrayList<Int>
)


