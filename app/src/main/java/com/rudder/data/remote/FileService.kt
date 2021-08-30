package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface FileService {
    @POST("/board/getUploadSignedUrls")
    suspend fun getUploadUrls(
        @Body getUploadUrlsInfo: GetUploadUrlsInfo
    ) : Response<JsonObject>

    @PUT
    suspend fun uploadImage(
        @Body file:RequestBody
    ) : Call<Void>
}

data class GetUploadUrlsInfo(
    val contentTypes:ArrayList<String>,
    val token:String
)