package com.rudder.data.local

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    fun setValue(key:String, value:String?){
        prefs.edit().putString(key,value).apply()
    }
    fun getValue(key:String):String?{
        return prefs.getString(key,"")
    }
}