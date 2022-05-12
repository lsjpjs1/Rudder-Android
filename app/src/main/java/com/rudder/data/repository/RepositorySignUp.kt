package com.rudder.data.repository

import com.google.gson.JsonObject
import com.rudder.data.Response
import com.rudder.data.SignUpInfo
import com.rudder.data.SignUpInsertInfo
import com.rudder.data.remote.SignUpApi
import com.rudder.util.ExceptionUtil

class RepositorySignUp {

    suspend fun signUpApiCall(singUpInfo : SignUpInfo) : Int {
        val apiResponse = SignUpApi.instance.signUpApiFun(singUpInfo).await()
        return apiResponse.code()
    }


    suspend fun signUpCreateAccount(signUpInsertInfo: SignUpInsertInfo) : Boolean { // Sign up, Complete!
        val serverFailJsonObject = JsonObject()
        serverFailJsonObject.addProperty("signUpComplete", false)
        val createAccountAPIResult = ExceptionUtil.retryWhenException(SignUpApi::createAccountSignUp,signUpInsertInfo,
            SignUpApi(),
            Response(serverFailJsonObject))
        return createAccountAPIResult.results.get("signUpComplete").asBoolean
    }


}