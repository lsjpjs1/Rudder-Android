package com.rudder.data.repository

import com.rudder.data.SignUpInfo
import com.rudder.data.remote.SignUpApi

class RepositorySignUp {

    suspend fun signUpApiCall(singUpInfo : SignUpInfo) : Int {
        val apiResponse = SignUpApi.instance.signUpApiFun(singUpInfo).await()
        return apiResponse.code()
    }

}