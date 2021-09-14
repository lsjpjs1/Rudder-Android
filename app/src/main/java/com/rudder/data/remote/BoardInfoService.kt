package com.rudder.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.rudder.data.CategorySelectMyPageInfo
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BoardInfoService {
    @POST("/board/categoryList")
    suspend fun getCategories() : Response<ArrayList<Category>>



    @POST("/board/userSelectCategoryList")
    suspend fun getSelectedCategories(
        @Body token: Token
    ) : Response<ArrayList<Category>>

}

data class Category(
    @SerializedName(value = "category_id")
    val categoryId : Int,
    @SerializedName(value = "category_name")
    val categoryName : String
)


data class Token(
    @SerializedName(value = "token")
    val token : String
    )

