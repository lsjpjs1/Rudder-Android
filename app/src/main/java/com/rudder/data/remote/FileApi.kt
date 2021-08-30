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
import okhttp3.RequestBody
import retrofit2.Call

class FileApi {
    companion object{
        val instance = FileApi()
    }
    private val fileService : FileService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(FileService::class.java)
    fun getUploadUrls(getUploadUrlsInfo: GetUploadUrlsInfo) : Deferred<Response<JsonObject>> {

        return GlobalScope.async(Dispatchers.IO){
            fileService.getUploadUrls(getUploadUrlsInfo)
        }
    }
    suspend fun uploadImage(file:RequestBody, signedUrl:String): Deferred<Call<Void>> {
        val fileTransferService : FileService = RetrofitClient.getClient(signedUrl).create(FileService::class.java)
        return GlobalScope.async(Dispatchers.IO) {
            fileTransferService.uploadImage(file)
        }
    }
}