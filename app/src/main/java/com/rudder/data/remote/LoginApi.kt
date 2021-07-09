package com.rudder.data.remote


import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import retrofit2.Call
import retrofit2.Response

class LoginApi {

    companion object{
        val instance = LoginApi()
    }

    private val loginService : LoginService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(LoginService::class.java)

    fun login(loginInfo: LoginInfo){

        val call : Call<JsonObject> = loginService.login(loginInfo)
        Log.d("",loginInfo.toString())
        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("fail",t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("success",response.body().toString())
            }
        })
    }


}