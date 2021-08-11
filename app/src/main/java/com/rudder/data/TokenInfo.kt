package com.rudder.data

import com.google.gson.annotations.SerializedName

data class TokenInfo(
    @SerializedName(value = "token")
    val token: String
)


