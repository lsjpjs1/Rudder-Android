package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailSingupAPI {
    @POST("/schoolverify/verifyEmail")
    fun emailPost(
            @Body email : Emailaddress
    ) : Call<String>

}

data class Emailaddress(val email : String)