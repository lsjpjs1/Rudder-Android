package com.rudder.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.ui.fragment.*
import com.rudder.util.ChangeUIState
import com.rudder.util.FragmentShowHide
import com.rudder.util.StartActivity
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }

    private lateinit var createAccountFragment : CreateAccountFragment
    private lateinit var profileSettingFragment : ProfileSettingFragment
    private lateinit var schoolSelectFragment: SchoolSelectFragment

    // id가 명시되어있지 않은 다른 부분을 터치했을 때 키보드가 보여져있는 상태면 키보드를 내림.
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAccountFragment = CreateAccountFragment()
        profileSettingFragment = ProfileSettingFragment()
        schoolSelectFragment = SchoolSelectFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.signUp_container, createAccountFragment)
            .hide(createAccountFragment)
            .add(R.id.signUp_container, profileSettingFragment)
            .hide(profileSettingFragment)
            .add(R.id.signUp_container, schoolSelectFragment)
            .commit()

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this

        val toastSignUpComplete = Toast.makeText(this, "Sign Up Complete!", Toast.LENGTH_SHORT)

        viewModel.callSchoolList()

        viewModel.schoolSelectNext.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                    fragmentShowHide.addToBackStack()
                    fragmentShowHide.showFragment(createAccountFragment, R.id.signUp_container)

                    verifyBtn.isEnabled = false
                    submitBtn.isEnabled = false
                    createAccountNextBtn.isEnabled = false
                    ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
                    viewModel.clearValue()
                }
            }})

        viewModel.schoolSelectBack.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) onBackPressed()
            }})

        viewModel.createAccountNext.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                    fragmentShowHide.addToBackStack()
                    fragmentShowHide.showFragment(profileSettingFragment, R.id.signUp_container)
                } }})

        viewModel.createAccountBack.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) onBackPressed()
            }})


        viewModel.profileSettingBack.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) onBackPressed()
            }})

        viewModel.profileSettingNext.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    StartActivity.callActivity(this, LoginActivity())
                    finish()
                    toastSignUpComplete.show()
                }
            }}) // signUP Complete !
    }


    override fun onResume() {
        Log.d("mytag","onResume")
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mytag","onDestory")
    }

//    override fun onBackPressed() {
//        if (schoolSelectFragment.isVisible) {
//            viewModel.clearValue()
//        }
//
//        super.onBackPressed()
//    }

}