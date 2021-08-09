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
import com.rudder.data.remote.*


class Repository {

    suspend fun login(loginInfo: LoginInfo) : Boolean{
        val key = BuildConfig.TOKEN_KEY
        var result = true
        Log.d("token",App.prefs.getValue(key).toString())
       if(App.prefs.getValue(key)==null || App.prefs.getValue(key)==""){
            val response =  LoginApi.instance.login(loginInfo).await()
            if(response.results.success){
                val value = response.results.token
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
        val response = PostApi.instance.addPostApi(addPostInfo).await()
        return response.results.isSuccess
    }

}