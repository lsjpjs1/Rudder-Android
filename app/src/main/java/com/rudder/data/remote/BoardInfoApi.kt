package com.rudder.data.remote

import android.util.Log
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BoardInfoApi {
    companion object{
        val instance = BoardInfoApi()
    }

    private val boardInfoService : BoardInfoService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(BoardInfoService::class.java)

    fun getCategoryList(getCategoriesRequest: GetCategoriesRequest) : Deferred<Response<ArrayList<Category>>> {
        return GlobalScope.async(Dispatchers.IO){
            if(getCategoriesRequest.schoolId==null) {
                boardInfoService.getCategories(GetCategoriesRequestWithToken(getCategoriesRequest.token))
            }else{
                boardInfoService.getCategories(GetCategoriesRequestWithSchoolId(getCategoriesRequest.schoolId))
            }
        }
    }

    fun getClubCategoryList(getCategoriesRequest: GetCategoriesRequest) : Deferred<Response<ArrayList<Category>>> {
        return GlobalScope.async(Dispatchers.IO){
            if(getCategoriesRequest.schoolId==null) {
                boardInfoService.getClubCategories(GetCategoriesRequestWithToken(getCategoriesRequest.token))
            }else{
                boardInfoService.getClubCategories(GetCategoriesRequestWithSchoolId(getCategoriesRequest.schoolId))
            }
        }
    }

    fun getSelectedCategoryListApi(token : Token) : Deferred<Response<ArrayList<Category>>> {
        return GlobalScope.async(Dispatchers.IO){
            boardInfoService.getSelectedCategories(token)
        }
    }



}