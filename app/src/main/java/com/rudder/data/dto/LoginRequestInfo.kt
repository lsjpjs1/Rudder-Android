package com.rudder.data.dto

data class LoginRequestInfo (
    val notificationToken: String,
    val os: String,
    val userId: String,
    val userPassword: String
)