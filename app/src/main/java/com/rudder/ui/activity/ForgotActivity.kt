package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.ActivityForgotBinding
import com.rudder.util.ProgressBarUtil
import com.rudder.viewModel.ForgotViewModel
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotActivity : AppCompatActivity() {
    private val viewModel: ForgotViewModel by lazy { ViewModelProvider(this).get(ForgotViewModel::class.java)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityForgotBinding>(this, R.layout.activity_forgot)
        binding.forgotVM = viewModel
        binding.lifecycleOwner = this


        val toastEmailTrue = Toast.makeText(this, "Complete to Send Your ID to Email Address", Toast.LENGTH_SHORT)
        val toastEmailFalse = Toast.makeText(this, "Not Valid Email Address", Toast.LENGTH_SHORT)
        val toastVerifyCodeTrue = Toast.makeText(this, "Complete to Send Your Password to Email Address", Toast.LENGTH_SHORT)
        val toastVerifyCodeFalse = Toast.makeText(this, "Not Valid Verification Code", Toast.LENGTH_SHORT)

        var findpasswordFlag = false

        viewModel.findIDClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot3.visibility = View.GONE
                    constraintLayoutForgot4.visibility = View.VISIBLE
                    findpasswordFlag = false
                } }
        })

        viewModel.findPasswordClick.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    constraintLayoutForgot4.visibility = View.GONE
                    constraintLayoutForgot3.visibility = View.VISIBLE
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


}