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


interface SignUpService{
    @POST("/schoolverify/verifyEmail")
    suspend fun emailSignUp(
        @Body email : EmailInfoSignUp
    ) : Response<JsonObject>

    @POST("/signupin/checkduplication")
    suspend fun idDuplicatedSignup(
        @Body userId : IdDuplicatedInfo
    ) : Response<JsonObject>

    @POST("/schoolverify/checkCode")
    suspend fun verifyCodeSignUp(
        @Body verifyInfo : CheckVerifyCodeInfo
    ) : Response<JsonObject>

    @POST("/signupin/schoolList")
    suspend fun schoolListSignUp(
    ) : Response<JsonArray>

    @POST("/signupin/signupinsert")
    suspend fun createAccountSignUp(
        @Body signUpInsertInfo : SignUpInsertInfo
    ) : Response<JsonObject>


    @POST("/signupin/checkDuplicationNickname")
    suspend fun nickNameDuplicatedService(
        @Body nickNameDuplicatedInfo: NickNameDuplicatedInfo
    ) : Response<JsonObject>


    @POST("/signupin/profileImageList")
    suspend fun profileImageListService() : Response<JsonObject>
}

data class School(
    val schoolId: Int,
    val schoolName: String
)




