package com.rudder.data

import com.google.gson.annotations.SerializedName

data class LoginInfo(
    @SerializedName(value = "user_id")
    val userId: String,
    @SerializedName(value = "user_password")
    val userPassword: String

)
