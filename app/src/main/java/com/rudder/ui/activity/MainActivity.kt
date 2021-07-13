package com.rudder.ui.activity

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.ActivityLoginBinding
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.fragment.CommunityFragment
import com.rudder.ui.fragment.MainBottomBarFragment
import com.rudder.ui.fragment.MyPageFragment
import com.rudder.viewModel.LoginViewModel
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel = MainViewModel
    private var mainBottomBarFragment = MainBottomBarFragment()
    private var communityFragment = CommunityFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainVM = viewModel
        binding.lifecycleOwner = this
        supportFragmentManager.beginTransaction().add(R.id.main_bottom_bar,mainBottomBarFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.main_display,communityFragment).commit()
        viewModel.selectedTab.observe(this, Observer {
            Log.d("abc",it.toString()+"    "+R.id.communityButton.toString()+"    "+R.id.myPageButton)
            when(it){
                R.id.communityButton -> {
                    showCommunity()
                    changeColorCommunity()
                }
                R.id.myPageButton -> {
                    showMyPage()
                    changeColorMyPage()
                }
            }
        })

    }

    fun showCommunity(){
        supportFragmentManager.beginTransaction().add(R.id.main_display,CommunityFragment()).commit()
    }

    fun showMyPage(){
        supportFragmentManager.beginTransaction().add(R.id.main_display,MyPageFragment()).commit()
    }

    fun changeColorCommunity(){
        val purpleRudder = ContextCompat.getColor(this,R.color.purple_rudder)
        val grey = ContextCompat.getColor(this,R.color.grey)
        mainBottomBarFragment.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.myPageIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }
    fun changeColorMyPage(){
        val purpleRudder = ContextCompat.getColor(this,R.color.purple_rudder)
        val grey = ContextCompat.getColor(this,R.color.grey)
        mainBottomBarFragment.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.communityIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }



}