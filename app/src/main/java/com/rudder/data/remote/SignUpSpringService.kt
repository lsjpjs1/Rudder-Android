package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.dto.SignUpInfo
import retrofit2.http.Body
import retrofit2.http.POST


interface SignUpSpringService{
    @POST("/user-infos")
    suspend fun signUpService(
        @Body singUpInfo : SignUpInfo
    ) : retrofit2.Response<JsonObject>
}




