package com.rudder.ui.activity

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rudder.R
import com.rudder.ui.fragment.MainBottomBarFragment
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.main_bottom_bar,MainBottomBarFragment()).commit()
    }
}