package com.rudder.data

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CategorySelectMyPageInfo(
    @SerializedName(value = "token")
    val token: String,
    @SerializedName(value = "categoryIdList")
    val categoryIdList: ArrayList<Int>
)



data class CategorySelectSignUpInfo(
    @SerializedName(value = "categoryIdList")
    val categoryIdList: ArrayList<Int>,
    @SerializedName(value = "user_id")
    val user_id: String
)


data class RequestCategoryInfo(
    @SerializedName(value = "token")
    val token: String,
    @SerializedName(value = "category_name")
    val requestCategoryName: String,
    @SerializedName(value = "requestBody")
    val requestBody: String

)
