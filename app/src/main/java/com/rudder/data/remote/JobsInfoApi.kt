package com.rudder.data.remote

import com.rudder.data.Response
import com.rudder.data.dto.JobsInfo
import retrofit2.http.Body
import retrofit2.http.POST

interface JobsInfoApi {
    @POST("/users/blockUser")
    suspend fun JobsInfoApiFun(
        @Body blockUserRequest: BlockUserRequest
    ) : Response<JobsInfo>
}

