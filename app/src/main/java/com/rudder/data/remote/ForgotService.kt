package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.EmailInfo
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotIDService {
    @POST("/signupin/sendIdToEmail")
    suspend fun forgotID(
            @Body emailInfo: EmailInfo

    ) : Response<JsonObject>
}



interface ForgotPasswordService {
    @POST("/signupin/sendPwVerificationCode")
    suspend fun forgotPassword(
        @Body emailInfo: EmailInfo

    ) : Response<JsonObject>
}