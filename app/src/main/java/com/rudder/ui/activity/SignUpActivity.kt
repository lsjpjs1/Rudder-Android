package com.rudder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_create_account.*


class SignUpActivity : AppCompatActivity() {
    private var viewModel: SignUpViewModel = SignUpViewModel

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

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this



        createAccountFragment = CreateAccountFragment()
        profileSettingFragment = ProfileSettingFragment()
        schoolSelectFragment = SchoolSelectFragment()


        viewModel.schoolSelectNext.observe(this, Observer {
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.showFragment(createAccountFragment,R.id.signUp_container)
        })


        viewModel.createAccountNext.observe(this, Observer {
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.showFragment(profileSettingFragment,R.id.signUp_container)
        }
        )

        viewModel.createAccountBack.observe(this, Observer {
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.showFragment(schoolSelectFragment,R.id.signUp_container)
        }
        )

        viewModel.profileSettingBack.observe(this, Observer {
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.showFragment(createAccountFragment,R.id.signUp_container)
        }
        )

    }

    override fun onResume() {

        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.signUp_container, createAccountFragment)
            .hide(createAccountFragment)
            .add(R.id.signUp_container, profileSettingFragment)
            .hide(profileSettingFragment)
            .add(R.id.signUp_container, schoolSelectFragment)

        transaction.commit()
        super.onResume()
    }
}