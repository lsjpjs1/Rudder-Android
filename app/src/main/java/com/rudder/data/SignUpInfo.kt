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


data class checkVeriCodeInfo(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "verifyCode")
    val veifyCode : String
)

