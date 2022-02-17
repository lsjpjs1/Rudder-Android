package com.rudder.data.remote

import com.google.gson.annotations.SerializedName

enum class ResponseEnum() {
    @SerializedName("")
    SUCCESS(),
    @SerializedName("duplicate")
    DUPLICATE(),
    @SerializedName("database")
    DATABASE(),
    @SerializedName("unknown")
    UNKNOWN(),
    @SerializedName("delete")
    DELETE(),
    @SerializedName("not exist")
    NOTEXIST()

}