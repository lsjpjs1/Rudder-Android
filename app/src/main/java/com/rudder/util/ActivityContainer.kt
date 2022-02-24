package com.rudder.util

import android.app.Activity
import android.app.Application

object ActivityContainer{
    var currentActivity : Activity? = null

    fun clearCurrentActivity(activity: Activity){
        currentActivity?.let{
            if(it.equals(activity)){
                currentActivity = null
            }
        }
    }
}