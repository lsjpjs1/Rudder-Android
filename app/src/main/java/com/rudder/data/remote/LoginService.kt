package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/signupin/loginJWT")
    suspend fun login(
            @Body loginInfo: LoginInfo

    ) : Response<JsonObject>
}

