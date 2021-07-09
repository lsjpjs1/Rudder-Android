package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/signupin/loginJWT")
    fun login(
            @Body loginInfo: LoginInfo
    ) : Call<JsonObject>

}