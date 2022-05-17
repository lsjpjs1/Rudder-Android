package com.rudder.data.local

import android.content.Context
import android.content.SharedPreferences


class MySharedPreferences(context: Context) {
    private val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)


    fun setValue(key:String, value:String?){ // apply
        prefs.edit().putString(key,value).apply()
    }
    fun getValue(key:String):String?{
        return prefs.getString(key,"")
    }

    fun removeValue(key:String){ // commit
        prefs.edit().remove(key).commit()
    }


    fun setValueCommit(key:String, value:String?) {
        prefs.edit().putString(key, value).commit()
    }


    fun setValueInt(key:String, value:Int?){ // apply
        prefs.edit().putInt(key,value!!).apply()
    }
    fun getValueInt(key:String):Int?{
        return prefs.getInt(key,0)
    }

    fun MyOnSharedPreferenceChangeListenerSetting(listener: SharedPreferences.OnSharedPreferenceChangeListener){
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }


}