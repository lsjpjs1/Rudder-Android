package com.rudder.ui.activity

import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_community_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_show_post.*
import java.util.*



class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy {ViewModelProvider(this).get(MainViewModel::class.java)  }
    private lateinit var mainBottomBarFragment : MainBottomBarFragment
    private lateinit var addCommentFragment: AddCommentFragment
    private lateinit var communityFragment : CommunityFragment
    private lateinit var myPageFragment : MyPageFragment
    private lateinit var addPostFragment: AddPostFragment
    private lateinit var showPostFragment : ShowPostFragment
    private lateinit var communityBottomSheetFragment : CommunityBottomSheetFragment
    private lateinit var communityPostReportFragment : CommunityPostReportFragment

    private lateinit var editPostFragment: EditPostFragment

    private val purpleRudder by lazy { ContextCompat.getColor(this,R.color.purple_rudder) }
    private val grey by lazy { ContextCompat.getColor(this,R.color.grey) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainVM = viewModel
        binding.lifecycleOwner = this


        mainBottomBarFragment = MainBottomBarFragment()
        communityFragment = CommunityFragment()
        myPageFragment = MyPageFragment()
        addPostFragment = AddPostFragment()
        showPostFragment = ShowPostFragment()
        addCommentFragment = AddCommentFragment()
        communityBottomSheetFragment = CommunityBottomSheetFragment()
        communityPostReportFragment = CommunityPostReportFragment()
        editPostFragment = EditPostFragment()


        supportFragmentManager.beginTransaction()
            .add(R.id.mainBottomBar,mainBottomBarFragment)
            .add(R.id.mainDisplay,myPageFragment,"myPage")
            .hide(myPageFragment)
            .add(R.id.mainDisplay,communityFragment,"community")
            .commit()


        viewModel.isPostMore.observe(this, Observer {
            if(it.getContentIfNotHandled()!!)
                communityBottomSheetFragment.show(supportFragmentManager, communityBottomSheetFragment.tag)
        })


        viewModel.isCommentMore.observe(this, Observer {
            if(it.getContentIfNotHandled()!!)
                communityBottomSheetFragment.show(supportFragmentManager, communityBottomSheetFragment.tag)
        })

        viewModel.isPostReport.observe(this, Observer {
            if(it.getContentIfNotHandled()!!)
                communityPostReportFragment.show(supportFragmentManager, communityBottomSheetFragment.tag)
        })


        viewModel.isPostEdit.observe(this, Observer {

            if(it.getContentIfNotHandled()!!){
                viewModel.clearAddPost()
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.addToBackStack()
                fragmentShowHide.addFragment(editPostFragment,R.id.mainDisplay,"editPost")
                fragmentShowHide.showFragment(editPostFragment,R.id.mainDisplay)
            }

        })


        viewModel.selectedTab.observe(this, Observer {
            when(it){
                R.id.communityButton -> {
                    FragmentShowHide(supportFragmentManager).showFragment(communityFragment,R.id.mainDisplay)
                    changeColorCommunity()
                }
                R.id.myPageButton -> {
                    FragmentShowHide(supportFragmentManager).showFragment(myPageFragment,R.id.mainDisplay)
                    changeColorMyPage()
                }
            }
        })

        viewModel.isAddPostClick.observe(this, Observer {
            if(it.getContentIfNotHandled()!!){
                viewModel.clearAddPost()
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.addToBackStack()
                fragmentShowHide.addFragment(addPostFragment,R.id.mainDisplay,"addPost")
                fragmentShowHide.showFragment(addPostFragment,R.id.mainDisplay)
            }
        })

        viewModel.isBackClick.observe(this, Observer {
            if(it.getContentIfNotHandled()!!){
                onBackPressed()
            }
        })
        viewModel.selectedPostPosition.observe(this, Observer {
            Log.d("select",it.toString())
        })

        viewModel.isScrollBottomTouch.observe(this, Observer {
            if(it.getContentIfNotHandled()!!){
                showProgressBar()
            }else{
                hideProgressBar()
            }
        })

        viewModel.startLoginActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                StartActivityUtil.callActivity(this, LoginActivity())
            }
        })

        viewModel.selectedCommentGroupNum.observe(this, Observer {
            if(it!=-1){
                showParentCommentInfo()
            }else{
                hideParentCommentInfo()
            }
        })

        viewModel.isAddPostSuccess.observe(this, Observer {
            super.onBackPressed()
        })

        viewModel.isLikePost.observe(this, Observer {

            if(it!!){
                showPostFragment?.showPostLikeImageView?.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            }else{
                showPostFragment?.showPostLikeImageView?.setImageResource(R.drawable.ic_outline_thumb_up_24)
            }
        })

    }

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

    override fun onBackPressed() {
        val isBackButtonAvailable = (!supportFragmentManager.findFragmentByTag("myPage")!!.isVisible) &&(!supportFragmentManager.findFragmentByTag("community")!!.isVisible)
        if(isBackButtonAvailable){ // 마이페이지 or 커뮤니티화면 아닐 때만 back버튼 활성화
            if(addCommentFragment.isVisible){
                swapMainBottomBar()
            }
            super.onBackPressed()
        } else {
            moveTaskToBack(true)
        }

    }

    fun swapMainBottomBar(){
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        fragmentShowHide.removeFragment(addCommentFragment)
        fragmentShowHide.showFragment(mainBottomBarFragment,R.id.mainBottomBar)
    }

    fun showParentCommentInfo(){
        parentCommentInfo.visibility = View.VISIBLE
    }
    fun hideParentCommentInfo(){
        parentCommentInfo.visibility = View.GONE
    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    fun expandProgressBarAnimation(){

    }

    fun changeSelectedPostPosition(position: Int){
        viewModel.setSelectedPostPosition(position)
    }

    fun showPost(){
        viewModel.isLikePost()
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        fragmentShowHide.addToBackStack()
        fragmentShowHide.addFragment(showPostFragment,R.id.mainDisplay,"showPost")
        fragmentShowHide.showFragment(showPostFragment,R.id.mainDisplay)
    }


    fun showAddComment(){
        addCommentFragment = AddCommentFragment()
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        fragmentShowHide.addFragment(addCommentFragment,R.id.mainBottomBar,"mainBottomBar")
        fragmentShowHide.showFragment(addCommentFragment,R.id.mainBottomBar)
    }


    fun changeColorCommunity(){
        mainBottomBarFragment.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.myPageIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }
    fun changeColorMyPage(){
        mainBottomBarFragment.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.communityIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("mytag","onDestorymain")
    }


}