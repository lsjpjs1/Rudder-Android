package com.rudder.data.remote

import com.rudder.data.LoginInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/signupin/loginJWT")
    suspend fun login(
            @Body loginInfo: LoginInfo

    ) : Response<LoginResponse>
}

data class LoginResponse(
    val success:Boolean,
    val error:String,
    val token:String
)