package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.GetCommentInfo
import com.rudder.data.GetPostInfo
import com.rudder.data.Post
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class CommentApi {
    companion object{
        val instance = CommentApi()
    }

    private val commentService : CommentService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(CommentService::class.java)

    fun getComments(getCommentInfo: GetCommentInfo) : Deferred<JsonObject> {

        return GlobalScope.async(Dispatchers.IO){
            commentService.renderComment(getCommentInfo)
        }

    }

}