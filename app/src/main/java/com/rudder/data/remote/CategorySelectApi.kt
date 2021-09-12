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

class CategorySelectApi {

    companion object{
        val instance = CategorySelectApi()
    }

    private val categorySelectService : CategorySelectService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(CategorySelectService::class.java)

    fun categorySelectApi(categorySelectInfo: CategorySelectInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            categorySelectService.categorySelectService(categorySelectInfo)
        }
    }

}