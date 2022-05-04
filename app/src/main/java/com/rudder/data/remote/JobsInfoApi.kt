package com.rudder.data.remote

import com.rudder.BuildConfig

class JobsInfoApi {

    companion object{
        val instance = JobsInfoApi()
    }


    val jobsInfoService : JobsInfoService = RetrofitClientSpring.getClient(BuildConfig.BASE_URL_SPRING).create(JobsInfoService::class.java)



}

