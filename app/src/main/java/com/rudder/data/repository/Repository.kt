package com.rudder.data.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.local.App
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi
import com.rudder.data.remote.SignUpApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        val key = BuildConfig.TOKEN_KEY
        var result = true
        if(App.prefs.getValue(key)==null || App.prefs.getValue(key)==""){
                val resLogin =  LoginApi.instance.login(loginInfo).await()
                if(resLogin.has("info")){
                    val value = resLogin.get("info").asString
                    App.prefs.setValue(key, value)
                }else{
                    result = false
                }
        }
        return result
    }

    suspend fun signUpSendVerifyCode(emailInfo : EmailInfo) : Boolean{
        var emailCheckFlag : Boolean
        val verifyAPIResult = SignUpApi.instance.emailSignUp(emailInfo).await()
        Log.d(TAG, "callPostTransferEmail : ${verifyAPIResult}")
        emailCheckFlag = verifyAPIResult == "true"

        return emailCheckFlag
    }

    suspend fun signUpIdDuplicated(idDuplicatedInfo: IdDuplicatedInfo) : Boolean{
        var idCheckFlag : Boolean
        val idDuplicatedAPIResultJson = SignUpApi.instance.idDuplicatedSignUp(idDuplicatedInfo).await()
        Log.d(TAG, "idDuplicatedAPIResultJson : ${idDuplicatedAPIResultJson}")

        val jsonResult = idDuplicatedAPIResultJson.getAsJsonObject("results")
        val isDuplicatedResult = jsonResult.get("isDuplicated").asBoolean

        idCheckFlag = isDuplicatedResult
        return idCheckFlag
    }

    suspend fun signUpCheckVerifyCode(checkVeriCodeInfo: CheckVerifyCodeInfo) : Boolean {
        var verifyCheckFlag : Boolean
        val checkVerifyAPIResult = SignUpApi.instance.checkVerifySignUp(checkVeriCodeInfo).await()
        Log.d(TAG, "checkVerifyAPIResult : ${checkVerifyAPIResult}")
        verifyCheckFlag = checkVerifyAPIResult == "Success"

        return verifyCheckFlag
    }

    suspend fun signUpCreateAccount(accountInfo: AccountInfo) : Boolean {
        var createAccountCheckFlag : Boolean
        val createAccountAPIResult = SignUpApi.instance.createAccountSignUp(accountInfo).await()
        Log.d(TAG, "createAccountAPIResult : ${createAccountAPIResult}")
        createAccountCheckFlag = createAccountAPIResult == "true"

        return createAccountCheckFlag
    }


    suspend fun getPosts(pagingIndex:Int, endPostId:Int): ArrayList<Post>{
        return PostApi.instance.getPosts(pagingIndex, endPostId).await()
    }

}