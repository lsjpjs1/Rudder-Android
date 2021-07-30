package com.rudder.data.remote


import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.rudder.BuildConfig
import com.rudder.data.CheckVerifyCodeInfo
import com.rudder.data.EmailInfo
import com.rudder.data.IdDuplicatedInfo
import kotlinx.coroutines.*
import retrofit2.Call

class SignUpApi {

    companion object{
        val instance = SignUpApi()
    }

    private val singUpEmailService : SingUpEmailService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SingUpEmailService::class.java)

    fun emailSignUp(emailInfo: EmailInfo) : Deferred<String>{
        return GlobalScope.async(Dispatchers.IO){
            singUpEmailService.emailSignUp(emailInfo)
        } }


    private val idDuplicatedService : IdDuplicatedService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(IdDuplicatedService::class.java)

    fun idDuplicatedSignUp(idDuplicatedInfo: IdDuplicatedInfo) : Deferred<JsonObject>{
        return GlobalScope.async(Dispatchers.IO){
            idDuplicatedService.idDuplicatedSignup(idDuplicatedInfo)
        } }


    private val checkVerifyCodeService : CheckVerifyCodeService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(CheckVerifyCodeService::class.java)

    fun checkVerifySignUp(checkVeriCodeInfo: CheckVerifyCodeInfo) : Deferred<String>{
        return GlobalScope.async(Dispatchers.IO){
            checkVerifyCodeService.verifyCodeSignUp(checkVeriCodeInfo)
        } }

}