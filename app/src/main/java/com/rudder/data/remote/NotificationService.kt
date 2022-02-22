package com.rudder.data.remote

import com.rudder.data.Response
import com.rudder.data.dto.NotificationItem
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("/notificationApi/getNotifications")
    suspend fun getNotifications(
        @Body getNotificationsRequest: GetNotificationsRequest
    ) : Response<GetNotificationsResponse>
}


data class GetNotificationsRequest(
    val token: String
)

data class GetNotificationsResponse(
    val isSuccess : Boolean,
    val error : ResponseEnum,
    val notifications : ArrayList<NotificationItem>
)
