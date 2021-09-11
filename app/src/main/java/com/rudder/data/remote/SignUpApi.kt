package com.rudder.data.remote


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.rudder.BuildConfig
import com.rudder.data.*
import kotlinx.coroutines.*
import org.json.JSONArray
import retrofit2.Call

class SignUpApi {

    companion object{
        val instance = SignUpApi()
    }
    private val singUpService : SignUpService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SignUpService::class.java)


    fun emailSignUp(emailInfoSignUp: EmailInfoSignUp) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.emailSignUp(emailInfoSignUp)
        } }

    fun idDuplicatedSignUp(idDuplicatedInfo: IdDuplicatedInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.idDuplicatedSignup(idDuplicatedInfo)
        } }

    fun checkVerifySignUp(checkVeriCodeInfo: CheckVerifyCodeInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.verifyCodeSignUp(checkVeriCodeInfo)
        } }


    fun schoolListSignUp() : Deferred<Response<JsonArray>> {
        return GlobalScope.async(Dispatchers.IO){
            singUpService.schoolListSignUp()
        } }


    fun createAccountSignUp(signUpInsertInfo: SignUpInsertInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.createAccountSignUp(signUpInsertInfo)
        } }


    fun nickNameDuplicatedSignUpApi(nickNameDuplicatedInfo: nickNameDuplicatedInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.nickNameDuplicatedService(nickNameDuplicatedInfo)
        } }

}