package com.rudder.data

import com.google.gson.annotations.SerializedName

data class EmailInfo(
    @SerializedName(value = "email")
    val email: String
)

data class IdDuplicatedInfo(
    @SerializedName(value = "user_id")
    val user_id: String
)


data class CheckVerifyCodeInfo(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "verifyCode")
    val verifyCode : String
)


data class AccountInfo(
    @SerializedName(value = "user_id")
    val userId: String,
    @SerializedName(value = "user_password")
    val userPassword: String

)
