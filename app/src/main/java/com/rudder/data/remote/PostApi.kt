package com.rudder.data.remote



import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.GetPostInfo
import com.rudder.data.PreviewPost
import com.rudder.data.Response
import kotlinx.coroutines.*
import kotlin.Exception
import kotlin.reflect.KFunction

class PostApi {

    companion object{
        val instance = PostApi()
    }

    private val postService : PostService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(PostService::class.java)




    fun getPosts(getPostInfo: GetPostInfo) : Deferred<ArrayList<PreviewPost>> {
        return GlobalScope.async(Dispatchers.IO){
                postService.renderPost(getPostInfo)

        }
    }

    fun addPostApi(addPostInfo: AddPostInfo):Deferred<Response<AddPostResponse>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addPost(addPostInfo)
        }
    }

    fun isLikePost(isLikePostInfo:IsLikePostInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.isLikePost(isLikePostInfo)
        }
    }

    fun addLikePost(addLikePostInfo: AddLikePostInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addLikePost(addLikePostInfo)
        }
    }

    fun addPostViewCount(addPostViewCountInfo: AddPostViewCountInfo):Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            postService.addPostViewCount(addPostViewCountInfo)
        }
    }

}