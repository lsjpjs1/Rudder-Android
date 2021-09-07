package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.*
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportService {
    @POST("/reviewsearch/sendReport")
    suspend fun reportService(
            @Body reportInfo: ReportInfo
    ) : Response<JsonObject>
}