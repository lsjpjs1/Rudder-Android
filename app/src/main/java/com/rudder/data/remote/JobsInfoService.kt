package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.dto.JobsDetail
import retrofit2.http.*


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
    suspend fun jobsByJobIdApiFun(
        @Header("Authorization") token : String,
        @Path("jobId") jobId: Int?
    ) : retrofit2.Response<JobsDetail>


    @GET("/jobs/my-favorite")
    suspend fun jobsGetMyFavoriteApiFun(
        @Header("Authorization") token : String,
        @Query("endJobId") endPostId: Int?,
    ) : retrofit2.Response<JsonObject>



    @POST("/jobs/{jobId}/favorite")
    suspend fun jobsFavoriteClickApiFun(
        @Header("Authorization") token : String,
        @Path("jobId") jobId: Int?
    ) : retrofit2.Response<JsonObject>


    @DELETE("/jobs/{jobId}/favorite")
    suspend fun jobsUnFavoriteClickApiFun(
        @Header("Authorization") token : String,
        @Path("jobId") jobId: Int?
    ) : retrofit2.Response<JsonObject>


}


data class JobsListRequest(
    val endPostId:Int,
    val searchBody : String
)