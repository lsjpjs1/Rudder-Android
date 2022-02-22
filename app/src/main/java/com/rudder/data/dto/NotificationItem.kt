package com.rudder.data.dto

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

enum class NotificationType(val typeNumber: Int,val alertMessage : String){
    @SerializedName("1")
    COMMENT(1,"New comment at your post!"),
    @SerializedName("2")
    POST_MESSAGE(2,"New message!"),
    @SerializedName("3")
    NESTED_COMMENT(3,"New comment at your comment!")


}
data class NotificationItem(
    val notificationId: Int,
    val notificationType: NotificationType,
    val itemId : Int,
    val itemBody : String,
    val itemTime : Timestamp
)
