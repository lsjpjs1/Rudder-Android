package com.rudder.data.dto

import java.sql.Timestamp

data class PostMessage(
    val receiveUserInfoId : Int,
    val sendUserInfoId : Int,
    val postMessageBody: String,
    val isRead : Boolean,
    val messageSendTime : Timestamp,
    val postMessageId : Int,
    val isSender : Boolean
)


data class PostMessageRoom(
        val postMessageRoomId : Int,
        val messageSendTime : Timestamp,
        val postMessageBody : String,
        val userId : String
)
