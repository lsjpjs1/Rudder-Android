package com.rudder.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.ui.fragment.*
import com.rudder.util.ChangeUIState
import com.rudder.util.FragmentShowHide
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.verifyBtn


class SignUpActivity : AppCompatActivity() {
    private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel::class.java) }

    //private val viewModel: MainViewModel by lazy {ViewModelProvider(this).get(MainViewModel::class.java)  }
    // merge 전 210914

    private lateinit var termsOfServiceFragment : TermsOfServiceFragment
    private lateinit var createAccountFragment : CreateAccountFragment
    private lateinit var profileSettingFragment : ProfileSettingFragment
    private lateinit var schoolSelectFragment: SchoolSelectFragment
    private lateinit var categorySelectSignUpFragment: CategorySelectSignUpFragment


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


    fun getDisplaySize():ArrayList<Int>{
        return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            val windowMetrics = this@SignUpActivity.windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left - insets.right
            val height = windowMetrics.bounds.height() - insets.top - insets.bottom
            arrayListOf(width,height)
        }else{
            val displayMertrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMertrics)
            val width = displayMertrics.widthPixels
            val height = displayMertrics.heightPixels
            arrayListOf(width,height)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        termsOfServiceFragment = TermsOfServiceFragment()
        createAccountFragment = CreateAccountFragment()
        profileSettingFragment = ProfileSettingFragment()
        schoolSelectFragment = SchoolSelectFragment()
        categorySelectSignUpFragment = CategorySelectSignUpFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.signUp_container, termsOfServiceFragment)
            .add(R.id.signUp_container, schoolSelectFragment)
            .hide(schoolSelectFragment)
            .add(R.id.signUp_container, createAccountFragment)
            .hide(createAccountFragment)
            .add(R.id.signUp_container, profileSettingFragment)
            .hide(profileSettingFragment)

//            .add(R.id.signUp_container, categorySelectSignUpFragment)
//            .hide(categorySelectSignUpFragment)

            .commit()

        val binding = DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.signUpVM = viewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val toastSignUpComplete = Toast.makeText(this, "Sign Up Complete!", Toast.LENGTH_LONG)


        ProgressBarUtil.progressBarFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it)
                    ProgressBarUtil.progressBarVisibleActivity(progressBarSignUP, this)
                else
                    ProgressBarUtil.progressBarGoneActivity(progressBarSignUP, this)
            }
        })


        viewModel.termsOfServiceNext.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                    fragmentShowHide.addToBackStack()
                    fragmentShowHide.showFragment(schoolSelectFragment, R.id.signUp_container)
                } }
        })

        viewModel.termsOfServiceBack.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if (it) onBackPressed()
            }})


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
                    viewModel.clearEmailValue()
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
                    toastSignUpComplete.show()
                    val fragmentShowHide = FragmentShowHide(supportFragmentManager)

                    fragmentShowHide.removeFragment(termsOfServiceFragment)
                    fragmentShowHide.removeFragment(schoolSelectFragment)
                    fragmentShowHide.removeFragment(createAccountFragment)
                    fragmentShowHide.removeFragment(profileSettingFragment)


                    fragmentShowHide.addFragment(categorySelectSignUpFragment,R.id.signUp_container,"categorySelectSignUpFragment")
                    fragmentShowHide.showFragment(categorySelectSignUpFragment, R.id.signUp_container)
                }
            }})  // signUP Complete !



        viewModel.categorySelectBack.observe(this, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                StartActivityUtil.callActivity(this, LoginActivity())
                finish()
            }})


        viewModel.categorySelectApply.observe(this, Observer { // Apply 버튼
            it.getContentIfNotHandled()?.let{ it ->
                if (it) {
                    StartActivityUtil.callActivity(this, LoginActivity())
                    finish()
                }
            }})

    }




    override fun onBackPressed() {
        if(categorySelectSignUpFragment.isVisible) {
            StartActivityUtil.callActivity(this, LoginActivity())
            finish()
        } else
            super.onBackPressed()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}