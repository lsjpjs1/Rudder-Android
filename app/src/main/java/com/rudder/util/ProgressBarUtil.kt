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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rudder.R
import kotlinx.android.synthetic.main.activity_sign_up.*

object ProgressBarUtil {

    val _progressBarFlag = MutableLiveData<Event<Boolean>>()
    val progressBarFlag : LiveData<Event<Boolean>> = _progressBarFlag


    val _progressBarMiniFlag = MutableLiveData<Event<Boolean>>()
    val progressBarMiniFlag : LiveData<Event<Boolean>> = _progressBarMiniFlag


    val _progressBarDialogFlag = MutableLiveData<Event<Boolean>>()
    val progressBarDialogFlag : LiveData<Event<Boolean>> = _progressBarDialogFlag


    fun progressBarVisibleActivity(progressBar: View,  activity: Activity){
        progressBar.visibility = View.VISIBLE
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }

    fun progressBarGoneActivity(progressBar: View,  activity: Activity){
        progressBar.visibility = View.GONE
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    fun progressBarVisibleFragment(progressBar: View, fragment: Fragment){
        progressBar.visibility = View.VISIBLE
        fragment.activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun progressBarGoneFragment(progressBar: View, fragment: Fragment){
        progressBar.visibility = View.GONE
        fragment.activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}

