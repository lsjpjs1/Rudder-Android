package com.rudder.data.remote

import com.rudder.BuildConfig

class JobsInfoApi {

    companion object{
        val instance = JobsInfoApi()
    }

    //private val blockUserService : BlockUserService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(BlockUserService::class.java)


    val jobsInfoService : JobsInfoService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(JobsInfoService::class.java)

//    fun blockUser(blockUserRequest: BlockUserRequest) : Deferred<Response<BlockUserResponse>> {
//        return GlobalScope.async(Dispatchers.IO){
//            blockUserService.blockUser(blockUserRequest)
//        }
//    }


}

