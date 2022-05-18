package com.rudder.data.repository

import android.util.Log
import com.rudder.BuildConfig
import com.rudder.data.dto.LoginRequestInfo
import com.rudder.data.local.App
import com.rudder.data.remote.LoginApi
import org.json.JSONObject


class RepositoryLogin {


    suspend fun checkTokenApiCall(token : String) : Int {
        val tokenDrop = token.drop(1).dropLast(1)
        val apiResponse = LoginApi.instance.tokenCheckServiceCall(token = "Bearer $tokenDrop").await()
        return apiResponse.code()
    }


    suspend fun loginApiCall(loginRequestInfo: LoginRequestInfo): Int {
        val apiResponse = LoginApi.instance.loginServiceCall(loginRequestInfo).await()
        val key = BuildConfig.TOKEN_KEY
        //val userIdKey = "userId"

        if (apiResponse.isSuccessful) { // 클릭 로그인 성공시
            val body = apiResponse.body()
            val bodyToken = body?.get("accessToken").toString()

            val tokenDrop = bodyToken.drop(1).dropLast(1)
            App.prefs.setValue(key, "Bearer $tokenDrop")

            //App.prefs.setValue(userIdKey, loginRequestInfo.userId)
            return apiResponse.code()

        } else { // 클릭 로그인 실패시
            val errorBody = apiResponse.errorBody() // 엘비스 표현식?

            if (errorBody == null) {
                return -1
            } else {
                val errorJsonObject = JSONObject(errorBody.string())
                val errorCodeString = errorJsonObject.getString("code")
                Log.d("errorCodeString", "${errorCodeString}")
                when (errorCodeString) {
                    "EMAIL_NOT_VERIFIED" -> return 401
                    "PASSWORD_WRONG" -> return 402
                    "USER_ID_NOT_FOUND" -> return 404
                    "BAD_REQUEST_CONTENT" -> return 406
                    else -> return -1
            }

            }
        }

//        if (App.prefs.getValue(key) == null || App.prefs.getValue(key) == "") { // 토큰이 비어있는 상태, 로그인의 서버요청이 필요한 상태
//            //val apiResponse = LoginApi.instance.loginServiceCall(loginRequestInfo).await()
//        } else {
//            Log.d("test123", "autologintokenCheck")
//            checkTokenApiCall(App.prefs.getValue(key)!!)
//
//            return 404
//        }

    }




//    suspend fun login(loginInfo: LoginInfo) : Boolean{
//        try{
//            val key = BuildConfig.TOKEN_KEY
//            val result : Boolean
//            val userIdKey = "userId"
//
//            if(App.prefs.getValue(key) == null || App.prefs.getValue(key) == "" ){ // 토큰이 비어있는 상태, 로그인의 서버요청이 필요한 상태
//                val resLogin =  LoginApi.instance.login(loginInfo).await()
//                if (resLogin.results.success){
//                    val loginToken = resLogin.results.token
//                    App.prefs.setValue(key, loginToken)
//                    App.prefs.setValue(userIdKey,loginInfo.userId)
//                    result = true
//                } else{
//                    result = false
//                }
//            } else { //      자동 로그인 하는 상태, 로그인의 서버요청이 필요하지 않는 상태, 토큰이 차있는 상태, 토큰 유효 검사 해야됨.
//                result = checkToken(TokenInfo(App.prefs.getValue(key)!!))
//            }
//            return result
//        }catch (e: Exception){
//            e.printStackTrace()
//            return false
//        }
//
//    }

}