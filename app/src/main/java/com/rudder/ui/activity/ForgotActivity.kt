package com.rudder.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.rudder.R
import com.rudder.databinding.ActivityForgotBinding
import com.rudder.util.ActivityContainer
import com.rudder.util.ProgressBarUtil
import com.rudder.viewModel.ForgotViewModel
import kotlinx.android.synthetic.main.activity_forgot.*

class ForgotActivity : AppCompatActivity() {
    private val forgotViewModel: ForgotViewModel by lazy { ViewModelProvider(this).get(ForgotViewModel::class.java)  }

    private var _binding: ActivityForgotBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityContainer.currentActivity = this

        _binding = DataBindingUtil.setContentView<ActivityForgotBinding>(this, R.layout.activity_forgot)
        binding.forgotVM = forgotViewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val toastEmailTrue = Toast.makeText(this, "We have sent your ID to your university email", Toast.LENGTH_SHORT)
        val toastEmailVerifySuccess = Toast.makeText(this, "We have sent the verification code to your university email", Toast.LENGTH_SHORT)
        val toastEmailVerifyFail = Toast.makeText(this, "Please enter your valid university email ", Toast.LENGTH_SHORT)
        val toastEmailFalse = Toast.makeText(this, "Please enter valid your university email.", Toast.LENGTH_SHORT)
        val toastVerifyCodeTrue = Toast.makeText(this, "Correct Code! We will send new password to your email.", Toast.LENGTH_SHORT)
        val toastVerifyCodeFalse = Toast.makeText(this, "Wrong verification code", Toast.LENGTH_SHORT)
        val toastFindId = Toast.makeText(this, "Tell us your university email. Then we will send your id to your email.", Toast.LENGTH_SHORT)
        var findpasswordFlag = false

        toastFindId.show()

        binding.forgotTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val pos = tab!!.position
                changeView(pos)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val pos = tab!!.position
                changeView(pos)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val pos = tab!!.position
                changeView(pos)
            }
        })


        forgotViewModel.findIDClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot3.visibility = View.GONE
                    constraintLayoutForgot4.visibility = View.VISIBLE
                    findpasswordFlag = false
                } }
        })

        forgotViewModel.findPasswordClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot4.visibility = View.GONE
                    constraintLayoutForgot3.visibility = View.VISIBLE
                    findpasswordFlag = true
                } }
        })

        forgotViewModel.emailCheckFlag.observe(this, Observer {
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


        forgotViewModel.emailVerifyFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    toastEmailVerifySuccess.show()
                    if (findpasswordFlag) forgotSendPasswordBtn.isEnabled = true
                }
                else {
                    toastEmailVerifyFail.show()
                    if (findpasswordFlag) forgotSendPasswordBtn.isEnabled = false
                }
            }
        })


        forgotViewModel.verifyCodeCheckFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) toastVerifyCodeTrue.show()
                else toastVerifyCodeFalse.show()
            }
        })

        forgotViewModel.isBackClick.observe(this, Observer {
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

    }


    private fun changeView(index: Int) {
        when (index) {
            0 -> {
                binding.constraintLayoutForgot4.visibility = View.VISIBLE
                binding.constraintLayoutForgot3.visibility = View.INVISIBLE
                val toastFindId = Toast.makeText(this, "Tell us your university email. Then we will send your id to your email.", Toast.LENGTH_SHORT)
                toastFindId.show()
                forgotViewModel.clickID()
            }
            1 -> {
                binding.constraintLayoutForgot4.visibility = View.INVISIBLE
                binding.constraintLayoutForgot3.visibility = View.VISIBLE
                val toastFindPassword = Toast.makeText(this, "Tell us your university email. Then we will send verification code.", Toast.LENGTH_SHORT)
                toastFindPassword.show()
                forgotViewModel.clickPassword()
            }
        }
    }


    override fun onDestroy() {
        ActivityContainer.clearCurrentActivity(this)
        super.onDestroy()
    }

}