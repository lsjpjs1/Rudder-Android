package com.rudder.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class StartActivityUtil {

    companion object {
        fun callActivity(fromActivity: AppCompatActivity, toActivity: AppCompatActivity) {
            val intent = Intent(fromActivity, toActivity::class.java)
            startActivity(fromActivity, intent, null)
        }

    }
}

