package com.rudder.data.remote


import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import com.rudder.data.dto.LoginRequestInfo
import kotlinx.coroutines.*

class LoginApi {

    companion object{
        val instance = LoginApi()
    }

    private val loginService : LoginService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(LoginService::class.java)
    private val loginSpringService : LoginSpringService = RetrofitClientSpring.getClient(BuildConfig.BASE_URL_SPRING).create(LoginSpringService::class.java)


    fun loginServiceCall(loginRequestInfo: LoginRequestInfo) :Deferred<retrofit2.Response<JsonObject>> {
        return CoroutineScope(Dispatchers.IO).async{ loginSpringService.loginService(loginRequestInfo) }
    }

    fun tokenCheckServiceCall(token : String) :Deferred<retrofit2.Response<JsonObject>> {
        return CoroutineScope(Dispatchers.IO).async{ loginSpringService.tokenCheckService(token) }
    }


    fun login(loginInfo: LoginInfo) : Deferred<Response<LoginResponse>>{

        return GlobalScope.async(Dispatchers.IO){
            loginService.login(loginInfo)
        }

    }


}