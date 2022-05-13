package com.rudder.data.remote


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.dto.SignUpInfo
import kotlinx.coroutines.*

class SignUpApi {

    companion object{
        val instance = SignUpApi()
    }
    private val singUpService : SignUpService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SignUpService::class.java)
    private val singUpSpringService : SignUpSpringService = RetrofitClientSpring.getClient(BuildConfig.BASE_URL_SPRING).create(SignUpSpringService::class.java)


    fun signUpApiFun(singUpInfo : SignUpInfo) : Deferred<retrofit2.Response<JsonObject>> {
        return CoroutineScope(Dispatchers.IO).async{ singUpSpringService.signUpService(singUpInfo) }
    }

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


    fun nickNameDuplicatedSignUpApi(nickNameDuplicatedInfo: NickNameDuplicatedInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.nickNameDuplicatedService(nickNameDuplicatedInfo)
        } }


    fun profileImageListSignUpApi() : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            singUpService.profileImageListService()
        } }
}