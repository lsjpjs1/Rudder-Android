package com.rudder.data.dto

import java.sql.Timestamp

data class PostMessage(
    val receiveUserInfoId : Int,
    val sendUserInfoId : Int,
    val messageBody : String,
    val isRead : Boolean,
    val messageSendTime : Timestamp,
    val postMessageId : Int,
    val sendUserNickname : String,
    val receiveUserNickname : String // 210115,,,,,

)
