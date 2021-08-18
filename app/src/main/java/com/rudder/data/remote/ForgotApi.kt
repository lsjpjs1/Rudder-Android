package com.rudder.data.remote


import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.await

class ForgotApi {

    companion object{
        val instance = ForgotApi()
    }

    private val forgotIDService : ForgotIDService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(ForgotIDService::class.java)

    fun findForgotID(emailInfo: EmailInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            forgotIDService.forgotID(emailInfo)
        }
    }


    private val forgotPasswordService : ForgotPasswordService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(ForgotPasswordService::class.java)

    fun findForgotPassword(emailInfo: EmailInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            forgotPasswordService.forgotPassword(emailInfo)
        }
    }


    private val sendPasswordSevice : SendPasswordSevice = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SendPasswordSevice::class.java)

    fun sendPassword(verifyInfo : CheckVerifyCodeInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            sendPasswordSevice.forgotSendPassword(verifyInfo)
        }
    }
}