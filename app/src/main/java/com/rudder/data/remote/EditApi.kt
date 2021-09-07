package com.rudder.data.remote


import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.*
import kotlinx.coroutines.*


class EditApi {

    companion object{
        val instance = EditApi()
    }

    private val editService : EditService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(EditService::class.java)

    fun editPostApi(editPostInfo: EditPostInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            editService.editPostService(editPostInfo)
        }
    }

    fun editCommentApi(editCommentInfo: EditCommentInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            editService.editCommentService(editCommentInfo)
        }
    }
    
    

}