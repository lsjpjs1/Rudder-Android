package com.rudder.data.remote


import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.await

class DeleteApi {

    companion object{
        val instance = DeleteApi()
    }

    private val deleteService : DeleteService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(DeleteService::class.java)

    fun deletePostApi(deletePostInfo : DeletePostInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            deleteService.deletePostService(deletePostInfo)
        }
    }

    fun deletecommentApi(deletecommentInfo : DeleteCommentInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            deleteService.deleteCommentService(deletecommentInfo)
        }
    }
    
    

}