package com.rudder.ui.activity

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_community_display.view.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy {ViewModelProvider(this).get(MainViewModel::class.java)  }
    private lateinit var mainBottomBarFragment : MainBottomBarFragment
    private lateinit var addCommentFragment: AddCommentFragment
    private lateinit var communityFragment : CommunityFragment
    private lateinit var myPageFragment : MyPageFragment
    private lateinit var addPostFragment: AddPostFragment
    private val showPostFragment = ShowPostFragment()
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
        addCommentFragment = AddCommentFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.mainBottomBar,mainBottomBarFragment)
            .add(R.id.mainDisplay,myPageFragment,"myPage")
            .hide(myPageFragment)
            .add(R.id.mainDisplay,communityFragment,"community")
            .commit()

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
        viewModel.posts.observe(this, Observer {
            Log.d("changedPost",it.toString())
        })

        viewModel.isAddPostClick.observe(this, Observer {
            if(it.getContentIfNotHandled()!!){
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

        viewModel.isScrollBottomTouch.observe(this, Observer {
            if(it.getContentIfNotHandled()!!){
                showProgressBar()
            }else{
                hideProgressBar()
            }
        })

    }

    override fun onBackPressed() {
        val isBackButtonAvailable = (!supportFragmentManager.findFragmentByTag("myPage")!!.isVisible) &&(!supportFragmentManager.findFragmentByTag("community")!!.isVisible)
        if(isBackButtonAvailable){ // 마이페이지 or 커뮤니티화면 아닐 때만 back버튼 활성화
            if(addCommentFragment.isVisible){
                Log.d("tag","visible")
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.removeFragment(addCommentFragment)
                fragmentShowHide.showFragment(mainBottomBarFragment,R.id.mainBottomBar)
            }
            super.onBackPressed()
        }
        Log.d("changedPost", supportFragmentManager.findFragmentByTag("1234").toString())
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



}