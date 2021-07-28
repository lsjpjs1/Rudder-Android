package com.rudder.data.repository

import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import com.rudder.data.local.App
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi


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

    suspend fun getPosts(pagingIndex:Int, endPostId:Int): ArrayList<Post>{


        return PostApi.instance.getPosts(pagingIndex, endPostId).await()
    }

}