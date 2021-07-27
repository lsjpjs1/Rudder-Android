package com.rudder.data.remote


import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.EmailInfo
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EmailSignupApi {

    companion object{
        val instance = EmailSignupApi()
    }

    private val emailSingupService : EmailSingupService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(EmailSingupService::class.java)

    fun emailSignup(emailInfo: EmailInfo) : Deferred<Call<String>>{

        return GlobalScope.async(Dispatchers.IO){
            emailSingupService.emailSignup(emailInfo)
        }

    }


}