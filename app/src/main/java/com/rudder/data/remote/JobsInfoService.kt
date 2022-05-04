package com.rudder.data.remote

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


//interface JobsInfoService {
//    @GET("/jobs")
//    suspend fun jobsInfoApiFun(
        //jobsListRequest: JobsListRequest
//    ) : Response<JobsInfo>
//}


interface JobsInfoService {
    @GET("/jobs")
    suspend fun jobsInfoApiFun(
        @Header("Authorization") token : String,
        @Query("endJobId") endPostId: Int?,
        @Query("searchBody") searchBody : String?
    ) : retrofit2.Response<JsonObject>



    @GET("/jobs/{jobId}")
    suspend fun jobsByIdApiFun(
        @Header("Authorization") token : String,
        @Path("jobId") jobId: Int?,
    ) : retrofit2.Response<JsonObject>



    @GET("/jobs/my-favorite")
    suspend fun jobsFavoriteApiFun(
        @Header("Authorization") token : String,
        @Query("endJobId") endPostId: Int?,
    ) : retrofit2.Response<JsonObject>


}


data class JobsListRequest(
    //val token: String,
    val endPostId:Int,
    val searchBody : String
)