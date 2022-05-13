package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.dto.LoginRequestInfo
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface LoginSpringService{
    @POST("/auth")
    suspend fun loginService(
        @Body loginRequestInfo : LoginRequestInfo
    ) : retrofit2.Response<JsonObject>


    @POST("/auth/validate")
    suspend fun tokenCheckService(
        @Header("Authorization") token : String,
    ) : retrofit2.Response<JsonObject>

}




