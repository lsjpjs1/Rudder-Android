package com.rudder.data

import com.google.gson.annotations.SerializedName

data class EmailInfo(
    @SerializedName(value = "email")
    val email: String
)

