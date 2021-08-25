package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BoardInfoApi {
    companion object{
        val instance = BoardInfoApi()
    }

    private val boardInfoService : BoardInfoService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(BoardInfoService::class.java)

    fun getCategoryList() : Deferred<Response<ArrayList<Category>>> {

        return GlobalScope.async(Dispatchers.IO){
            boardInfoService.getCategories()
        }
    }

}