package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.*
import retrofit2.http.Body
import retrofit2.http.POST

interface CategorySelectService {
    @POST("/board/addUserSelectCategory")
    suspend fun categorySelectService(
            @Body categorySelectInfo: CategorySelectInfo
    ) : Response<JsonObject>
}