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
    ) : Response<JsonObject>
}

interface IdDuplicatedService {
    @POST("/signupin/checkduplication")
    suspend fun idDuplicatedSignup(
        @Body userId : IdDuplicatedInfo
    ) : Response<JsonObject>
}

interface CheckVerifyCodeService {
    @POST("/schoolverify/checkCode")
    suspend fun verifyCodeSignUp(
        @Body verifyInfo : CheckVerifyCodeInfo
    ) : Response<JsonObject>
}


interface SchoolListService {
    @POST("/signupin/schoolList")
    suspend fun schoolListSignUp(
    ) : Response<JsonArray>
}

interface CreateAccountService {
    @POST("/signupin/signupinsert")
    suspend fun createAccountSignUp(
        @Body signUpInsertInfo : SignUpInsertInfo
    ) : Response<JsonObject>
}



