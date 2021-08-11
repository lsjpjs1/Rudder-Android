package com.rudder.data.remote


import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import com.rudder.data.TokenInfo
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.await

class TokenApi {

    companion object{
        val instance = TokenApi()
    }

    private val tokenService : TokenService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(TokenService::class.java)

    fun tokenValidation(tokenInfo : TokenInfo) : Deferred<Response<JsonObject>>{

        return GlobalScope.async(Dispatchers.IO){
            tokenService.tokenValid(tokenInfo)
        }

    }


}