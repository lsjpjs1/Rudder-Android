package com.rudder.data.dto

import java.sql.Timestamp

data class NotificationItem(
    val notificationBody : String,
    val notificationType : Int,
    val notificationId: Int,
)
