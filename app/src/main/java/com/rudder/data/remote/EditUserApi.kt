package com.rudder.data.remote

import com.rudder.BuildConfig
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditUserApi {
    companion object{
        val instance = EditUserApi()
    }

    private val editUserService : EditUserService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(EditUserService::class.java)

    fun updateNickname(updateNicknameRequest: UpdateNicknameRequest) : Deferred<Response<UpdateResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            editUserService.updateNickname(updateNicknameRequest)
        }
    }

    fun  updateProfileImage(updateProfileImageRequest: UpdateProfileImageRequest): Deferred<Response<UpdateResponse>> {
        return GlobalScope.async(Dispatchers.IO) {
            editUserService.updateProfileImage(updateProfileImageRequest)
        }
    }
}