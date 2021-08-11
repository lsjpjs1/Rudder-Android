package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import com.rudder.data.TokenInfo
import kotlinx.coroutines.Deferred
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("/signupin/validationToken")
    suspend fun tokenValid(
            @Body tokenInfo: TokenInfo
    ) : Response<JsonObject>

}