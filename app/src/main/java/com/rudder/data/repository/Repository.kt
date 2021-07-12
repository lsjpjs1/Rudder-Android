package com.rudder.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.local.App
import com.rudder.data.remote.LoginApi

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
}