package com.rudder.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.rudder.BuildConfig
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs


class ForecdTerminationService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) { //핸들링 하는 부분
        Log.d("Error", "onTaskRemoved - 강제 종료 $rootIntent")

        val autoLoginPref = prefs.getValue("autoLogin")
        val key = BuildConfig.TOKEN_KEY
        Log.d("autoLoginPref2", "$autoLoginPref")

        if (autoLoginPref == "false") { // 자동로그인을 안하고, 앱을 종료하면, 토큰 사라짐(로그아웃)
            prefs.setValue(key, "")
            Log.d("autoLoginPref4", "${prefs.getValue(key)}")
        }

        stopSelf() //서비스 종료
    }
}


