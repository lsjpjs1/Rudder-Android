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
    suspend fun getCategories(
        @Body getCategoriesRequestWithToken: GetCategoriesRequestWithToken
    ) : Response<ArrayList<Category>>

    @POST("/board/categoryList")
    suspend fun getCategories(
        @Body getCategoriesRequestWithSchoolId: GetCategoriesRequestWithSchoolId
    ) : Response<ArrayList<Category>>

    @POST("/board/clubCategoryList")
    suspend fun getClubCategories(
        @Body getCategoriesRequestWithToken: GetCategoriesRequestWithToken
    ) : Response<ArrayList<Category>>

    @POST("/board/clubCategoryList")
    suspend fun getClubCategories(
        @Body getCategoriesRequestWithSchoolId: GetCategoriesRequestWithSchoolId
    ) : Response<ArrayList<Category>>




    @POST("/board/userSelectCategoryList")
    suspend fun getSelectedCategories(
        @Body token: Token
    ) : Response<ArrayList<Category>>

}

data class Category(
    @SerializedName(value = "category_id")
    val categoryId : Int,

    @SerializedName(value = "category_name")
    val categoryName : String,

    val isMember : String?,

    @SerializedName(value = "category_type")
    val categoryType : String
)


data class Token(
    @SerializedName(value = "token")
    val token : String
    )

data class GetCategoriesRequest(
    val token: String?,
    @SerializedName(value = "school_id")
    val schoolId : Int?
)

data class GetCategoriesRequestWithToken(
    val token: String?
)

data class GetCategoriesRequestWithSchoolId(

    @SerializedName(value = "school_id")
    val schoolId : Int?
)

