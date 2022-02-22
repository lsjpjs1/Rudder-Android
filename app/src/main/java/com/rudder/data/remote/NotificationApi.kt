package com.rudder.data.remote

import com.rudder.BuildConfig
import com.rudder.data.Response
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class NotificationApi {
    companion object{
        val instance = NotificationApi()
    }

    private val notificationService : NotificationService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(NotificationService::class.java)

    fun getNotifications(getNotificationsRequest: GetNotificationsRequest) : Deferred<Response<GetNotificationsResponse>> {

        return GlobalScope.async(Dispatchers.IO){
            notificationService.getNotifications(getNotificationsRequest)
        }

    }



}