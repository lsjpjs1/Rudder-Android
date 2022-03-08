package com.rudder.data.remote

import com.google.gson.JsonObject
import com.rudder.data.*
import retrofit2.http.Body
import retrofit2.http.POST

interface CategorySelectService {
    @POST("/board/updateUserSelectCategory")
    suspend fun categorySelectSignUpService(
            @Body categorySelectSignUpInfo: CategorySelectSignUpInfo
    ) : Response<JsonObject>



    @POST("/board/updateUserSelectCategory")
    suspend fun categorySelectMyPageService(
        @Body categorySelectMyPageInfo: CategorySelectMyPageInfo
    ) : Response<JsonObject>


    @POST("/board/requestAddCategory")
    suspend fun requestCategoryService(
        @Body requestCategoryInfo: RequestCategoryInfo
    ) : Response<JsonObject>


}