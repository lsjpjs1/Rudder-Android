package com.rudder.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rudder.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class ProgressBarUtil() {

    companion object {

        val _progressBarFlag = MutableLiveData<Event<Boolean>>()
        val progressBarFlag : LiveData<Event<Boolean>> = _progressBarFlag

        fun progressBarVisible(progressBar: View, activityID: ViewGroup, colorResource: Int){
            progressBar.visibility = View.VISIBLE
            activityID.setBackgroundResource(colorResource)
        }

        fun progressBarGone(progressBar: View, activityID: ViewGroup, colorResource: Int){
            progressBar.visibility = View.GONE
            activityID.setBackgroundResource(colorResource)
        }

    }
}

