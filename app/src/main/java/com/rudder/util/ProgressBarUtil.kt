package com.rudder.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rudder.R
import kotlinx.android.synthetic.main.activity_sign_up.*

object ProgressBarUtil {

    val _progressBarFlag = MutableLiveData<Event<Boolean>>()
    val progressBarFlag : LiveData<Event<Boolean>> = _progressBarFlag

    fun progressBarVisible(progressBar: View, activityID: ViewGroup, colorResource: Int, activity: Activity){
        progressBar.visibility = View.VISIBLE
        activityID.setBackgroundResource(colorResource)
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }

    fun progressBarGone(progressBar: View, activityID: ViewGroup, colorResource: Int, activity: Activity){
        progressBar.visibility = View.GONE
        activityID.setBackgroundResource(colorResource)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}

