package com.rudder.data.repository

import com.google.gson.JsonObject
import com.rudder.data.LoginInfo
import com.rudder.data.remote.LoginApi
import kotlinx.coroutines.Deferred

class Repository {
    fun login(loginInfo: LoginInfo) : Deferred<JsonObject>{
        return LoginApi.instance.login(loginInfo)
    }
}