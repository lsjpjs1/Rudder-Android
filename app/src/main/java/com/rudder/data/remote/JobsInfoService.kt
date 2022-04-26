package com.rudder.data.remote

import com.rudder.BuildConfig

class JobsInfoService {

    companion object{
        val instance = JobsInfoService()
    }

    //private val blockUserService : BlockUserService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(BlockUserService::class.java)


    private val jobsInfoService : JobsInfoService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(JobsInfoService::class.java)

//    fun blockUser(blockUserRequest: BlockUserRequest) : Deferred<Response<BlockUserResponse>> {
//        return GlobalScope.async(Dispatchers.IO){
//            blockUserService.blockUser(blockUserRequest)
//        }
//    }


}