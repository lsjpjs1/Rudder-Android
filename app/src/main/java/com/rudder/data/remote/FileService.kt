package com.rudder.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.LoginInfo
import com.rudder.data.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FileService {
    @POST("/board/getUploadSignedUrls")
    suspend fun getUploadUrls(
        @Body getUploadUrlsInfo: GetUploadUrlsInfo
    ) : Response<JsonObject>

    @PUT
    suspend fun uploadImage(
        @Url path:String,@Body file:RequestBody
    ) : Call<Void>
}

data class GetUploadUrlsInfo(
    val contentTypes:ArrayList<String>,
    val token:String,
    @SerializedName("post_id")
    val postId:Int
)