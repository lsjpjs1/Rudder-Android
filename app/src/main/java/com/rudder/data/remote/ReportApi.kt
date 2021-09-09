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

class ReportApi {

    companion object{
        val instance = ReportApi()
    }

    private val reportService : ReportService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(ReportService::class.java)

    fun reportApi(reportInfo: ReportInfo) : Deferred<Response<JsonObject>>{
        return GlobalScope.async(Dispatchers.IO){
            reportService.reportService(reportInfo)
        }
    }

}