package com.rudder.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.JsonArray
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi
import com.rudder.data.remote.SignUpApi
import com.rudder.data.remote.TokenApi
import com.rudder.data.remote.*


class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        val key = BuildConfig.TOKEN_KEY
        val result : Boolean

        Log.d("login", "App.prefs.getValue(key) : ${prefs.getValue(key)}")

        if(prefs.getValue(key) == null || prefs.getValue(key) == "" ){ // 토큰이 비어있는 상태, 로그인의 서버요청이 필요한 상태
            val resLogin =  LoginApi.instance.login(loginInfo).await()
            val value = resLogin.results
            val flag = value.get("success").asBoolean
            Log.d("login", "value : $value")
            if (flag){
                val loginToken = value.get("token").asString
                prefs.setValue(key, loginToken)
                result = true
            } else{
                result = false
            }
        } else { //      자동 로그인 하는 상태, 로그인의 서버요청이 필요하지 않는 상태, 토큰이 차있는 상태, 토큰 유효 검사 해야됨.
            Log.d("login", "value : ${prefs.getValue(key)!!}")
            result = checkToken(TokenInfo(prefs.getValue(key)!!))

        }
        return result
    }


    suspend fun signUpSendVerifyCode(emailInfo : EmailInfo) : Boolean{
        val emailCheckFlag : Boolean
        val verifyAPIResult = SignUpApi.instance.emailSignUp(emailInfo).await()
        Log.d(TAG, "callPostTransferEmail : $verifyAPIResult")
        emailCheckFlag = verifyAPIResult == "true"

        return emailCheckFlag
    }

    suspend fun signUpIdDuplicated(idDuplicatedInfo: IdDuplicatedInfo) : Boolean{
        val idCheckFlag : Boolean
        val idDuplicatedAPIResultJson = SignUpApi.instance.idDuplicatedSignUp(idDuplicatedInfo).await()
        Log.d(TAG, "idDuplicatedAPIResultJson : $idDuplicatedAPIResultJson")

        val jsonResult = idDuplicatedAPIResultJson.getAsJsonObject("results")
        val isDuplicatedResult = jsonResult.get("isDuplicated").asBoolean

        idCheckFlag = isDuplicatedResult
        return idCheckFlag
    }

    suspend fun signUpCheckVerifyCode(checkVeriCodeInfo: CheckVerifyCodeInfo) : Boolean {
        val verifyCheckFlag : Boolean
        val checkVerifyAPIResult = SignUpApi.instance.checkVerifySignUp(checkVeriCodeInfo).await()
        Log.d(TAG, "checkVerifyAPIResult : $checkVerifyAPIResult")
        verifyCheckFlag = checkVerifyAPIResult == "Success"

        return verifyCheckFlag
    }

    suspend fun signUpCreateAccount(accountInfo: AccountInfo) : Boolean {
        val createAccountCheckFlag : Boolean
        val createAccountAPIResult = SignUpApi.instance.createAccountSignUp(accountInfo).await()
        Log.d(TAG, "createAccountAPIResult : $createAccountAPIResult")
        createAccountCheckFlag = createAccountAPIResult == "true"

        return createAccountCheckFlag
    }

    private suspend fun checkToken(tokenInfo: TokenInfo): Boolean {
        val tokenAPIResultJson = TokenApi.instance.tokenValidation(tokenInfo).await()
        Log.d(TAG, "tokenAPIResultJson : ${tokenAPIResultJson}")
        val jsonResult = tokenAPIResultJson.results
        Log.d(TAG, "tokenAPIResultJson : ${jsonResult}")
        return jsonResult.get("isTokenValid").asBoolean
    }


    suspend fun signUpSchoolList(): JsonArray {
        val schoolListFlagAPIResultJson = SignUpApi.instance.schoolListSignUp().await()
        Log.d(TAG, "schoolListFlagAPIResultJson : $schoolListFlagAPIResultJson")
        return schoolListFlagAPIResultJson.results
    }


    suspend fun getPosts(pagingIndex:Int, endPostId:Int): ArrayList<PreviewPost>{
        return PostApi.instance.getPosts(pagingIndex, endPostId).await()
    }

    suspend fun getComments(getCommentInfo: GetCommentInfo): ArrayList<Comment> {
        val resJson :Response<ArrayList<Comment>> = CommentApi.instance.getComments(getCommentInfo).await()
        return resJson.results
    }

    suspend fun addComment(addCommentInfo: AddCommentInfo) : Boolean{
        val resJson = CommentApi.instance.addComment(addCommentInfo).await()
        return resJson.results.get("isSuccess").asBoolean
    }

    suspend fun addPost(addPostInfo: AddPostInfo): Boolean{
        val response = PostApi.instance.addPostApi(addPostInfo).await()
        return response.results.isSuccess
    }

}