package com.rudder.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudder.BuildConfig
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import com.rudder.data.local.App
import com.rudder.data.remote.AddPostInfo
import com.rudder.data.remote.CommentApi
import com.rudder.data.remote.LoginApi
import com.rudder.data.remote.PostApi


class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        val key = BuildConfig.TOKEN_KEY
        var result = true
        Log.d("token",App.prefs.getValue(key).toString())
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

    suspend fun getComments(getCommentInfo: GetCommentInfo): ArrayList<Comment> {
        val resJson = CommentApi.instance.getComments(getCommentInfo).await()
        val type = object : TypeToken<ArrayList<Comment>>(){}
        return Gson().fromJson(resJson.get("results"),type.type)
    }

    suspend fun addPost(addPostInfo: AddPostInfo): Boolean{
        val resJson = PostApi.instance.addPostApi(addPostInfo).await()
        val type = object : TypeToken<ArrayList<Comment>>(){}
        return Gson().fromJson(resJson.get("results"),type.type)
    }

}