package com.rudder.data.dto

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

enum class NotificationType(val typeNumber: Int,val alertMessage : String){

    // 0 추가하면 안됨!!!!!!
    @SerializedName("1")
    COMMENT(1,"New comment at your post"),
    @SerializedName("2")
    POST_MESSAGE(2,"New Quick Mail"),
    @SerializedName("3")
    NESTED_COMMENT(3,"New comment at your comment");

    companion object{
        fun typeNumberToNotificationType(typeNumber: Int) : NotificationType?{
            for(notificationType in values()) {
                if (notificationType.typeNumber == typeNumber){
                    return notificationType
                }
            }
            return null
        }
    }


}
data class NotificationItem(
    val notificationId: Int,
    val notificationType: NotificationType,
    val itemId : Int,
    val itemBody : String,
    val itemTime : Timestamp
)
