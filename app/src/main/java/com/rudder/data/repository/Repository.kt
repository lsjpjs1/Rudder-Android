package com.rudder.data.repository

import com.rudder.data.LoginInfo
import com.rudder.data.remote.LoginApi

class Repository {
    fun login(loginInfo: LoginInfo){
        LoginApi.instance.login(loginInfo)
    }
}