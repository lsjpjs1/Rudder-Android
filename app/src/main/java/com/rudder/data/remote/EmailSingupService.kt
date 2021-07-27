package com.rudder.data.remote

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.rudder.data.EmailInfo

interface EmailSingupService {
    @POST("/schoolverify/verifyEmail")
    fun emailSignup(
            @Body email : EmailInfo
    ) : Call<String>

}
