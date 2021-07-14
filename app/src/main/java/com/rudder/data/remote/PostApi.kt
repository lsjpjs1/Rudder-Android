package com.rudder.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.Post
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PostApi {

    companion object{
        val instance = PostApi()
    }

    private val postService : PostService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(PostService::class.java)

    fun getPosts() : Deferred<ArrayList<Post>> {

        return GlobalScope.async(Dispatchers.IO){
            postService.renderPost("bulletin",0,-1)
        }

    }


}