package com.rudder.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.rudder.data.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import org.json.JSONObject

interface SingUpEmailService {
    @POST("/schoolverify/verifyEmail")
    suspend fun emailSignUp(
            @Body email : EmailInfo
    ) : String
}

interface IdDuplicatedService {
    @POST("/signupin/checkduplication")
    suspend fun idDuplicatedSignup(
        @Body userId : IdDuplicatedInfo
    ) : JsonObject
}

interface CheckVerifyCodeService {
    @POST("/schoolverify/checkCode")
    suspend fun verifyCodeSignUp(
        @Body verifyInfo : CheckVerifyCodeInfo
    ) : String
}

interface CreateAccountService {
    @POST("/signupin/signup")
    suspend fun createAccountSignUp(
        @Body accountInfo : AccountInfo
    ) : String
}


interface SchoolListService {
    @POST("/signupin/schoolList")
    suspend fun schoolListSignUp(
    ) : JSONArray
}



