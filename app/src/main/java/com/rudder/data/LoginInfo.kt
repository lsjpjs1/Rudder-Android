package com.rudder.data

import com.google.gson.annotations.SerializedName

data class LoginInfo( // node.js legacy
    @SerializedName(value = "user_id")
    val userId: String,
    @SerializedName(value = "user_password")
    val userPassword: String,
    @SerializedName(value = "notification_token")
    val notificationToken: String,
    @SerializedName(value = "os")
    val os: String
)


