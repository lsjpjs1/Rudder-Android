package com.rudder.data.remote

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.rudder.data.EmailInfo
import com.rudder.data.IdDuplicatedInfo
import com.rudder.data.checkVeriCodeInfo
import org.json.JSONObject

interface SingUpEmailService {
    @POST("/schoolverify/verifyEmail")
    suspend fun emailSignUp(
            @Body email : EmailInfo
    ) : String

}

interface IdDuplicatedService {
    @POST("/schoolverify/verifyEmail")
    suspend fun idDuplicatedSignup(
        @Body userId : IdDuplicatedInfo
    ) : JsonObject

}


interface checkVeriCodeService {
    @POST("/schoolverify/checkCode")
    suspend fun veriCodeSignUp(
        @Body veriInfo : checkVeriCodeInfo
    ) : Call<String>

}



