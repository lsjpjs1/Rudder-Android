package com.rudder.data.remote

import android.util.Log
import com.rudder.BuildConfig
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class BlockUserApi  {
    companion object{
        val instance = BlockUserApi()
    }

    private val blockUserService : BlockUserService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(BlockUserService::class.java)

    fun blockUser(blockUserRequest: BlockUserRequest) : Deferred<Response<BlockUserResponse>> {
        return GlobalScope.async(Dispatchers.IO){
            blockUserService.blockUser(blockUserRequest)
        }
    }





}