package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.rudder.R
import com.rudder.databinding.ActivityForgotBinding
import com.rudder.util.ProgressBarUtil
import com.rudder.viewModel.ForgotViewModel
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_forgot.view.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotActivity : AppCompatActivity() {
    private val viewModel: ForgotViewModel by lazy { ViewModelProvider(this).get(ForgotViewModel::class.java)  }

    private var _binding: ActivityForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //_binding = ActivityForgotBinding.inflate(layoutInflater)

        _binding = DataBindingUtil.setContentView<ActivityForgotBinding>(this, R.layout.activity_forgot)
        binding.forgotVM = viewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val toastEmailTrue = Toast.makeText(this, "Complete to Send Your ID to Email Address", Toast.LENGTH_LONG)
        val toastEmailFalse = Toast.makeText(this, "Not Valid Email Address", Toast.LENGTH_LONG)
        val toastVerifyCodeTrue = Toast.makeText(this, "Complete to Send Your Password to Email Address", Toast.LENGTH_LONG)
        val toastVerifyCodeFalse = Toast.makeText(this, "Not Valid Verification Code", Toast.LENGTH_LONG)

        var findpasswordFlag = false




        binding.forgotTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("onTabSelected","${tab}")
                val pos = tab!!.position
                changeView(pos)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d("onTabReselected","${tab}")
                val pos = tab!!.position
                changeView(pos)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("onTabUnselected","${tab}")
                val pos = tab!!.position
                changeView(pos)
            }
        })



        viewModel.findIDClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot3.visibility = View.GONE
                    constraintLayoutForgot4.visibility = View.VISIBLE
                    //forgotIdSelect.isEnabled = false
                    //forgotPasswordSelect.isEnabled = true
                    findpasswordFlag = false
                } }
        })

        viewModel.findPasswordClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot4.visibility = View.GONE
                    constraintLayoutForgot3.visibility = View.VISIBLE
                    //forgotPasswordSelect.isEnabled = false
                    //forgotIdSelect.isEnabled = true
                    findpasswordFlag = true
                } }
        })

        viewModel.emailCheckFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    toastEmailTrue.show()
                    if (findpasswordFlag) forgotSendPasswordBtn.isEnabled = true
                }
                else {
                    toastEmailFalse.show()
                    if (findpasswordFlag) forgotSendPasswordBtn.isEnabled = false
                }
            }
        })

        viewModel.verifyCodeCheckFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) toastVerifyCodeTrue.show()
                else toastVerifyCodeFalse.show()
            }
        })



        viewModel.isBackClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                super.onBackPressed()
            }
        })


        ProgressBarUtil.progressBarFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it)
                    ProgressBarUtil.progressBarVisibleActivity(progressBarForgot, this)
                else
                    ProgressBarUtil.progressBarGoneActivity(progressBarForgot, this)
            }
        })


        forgotSendPasswordBtn.isEnabled = false
        //forgotIdSelect.isEnabled = false

    }


    private fun changeView(index: Int) {
        when (index) {
            0 -> {
                binding.constraintLayoutForgot4.visibility = View.VISIBLE
                binding.constraintLayoutForgot3.visibility = View.INVISIBLE
                viewModel.clickID()
            }
            1 -> {
                binding.constraintLayoutForgot4.visibility = View.INVISIBLE
                binding.constraintLayoutForgot3.visibility = View.VISIBLE
                viewModel.clickPassword()
            }
        }
    }


}