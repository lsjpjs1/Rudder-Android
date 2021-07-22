package com.rudder.ui.activity

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.data.Post
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_community_display.view.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {
    private var viewModel: MainViewModel = MainViewModel
    private lateinit var mainBottomBarFragment : MainBottomBarFragment
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
            val fragmentShowHide = FragmentShowHide(supportFragmentManager)
            fragmentShowHide.addToBackStack()
            fragmentShowHide.addFragment(addPostFragment,R.id.mainDisplay,"addPost")
            fragmentShowHide.showFragment(addPostFragment,R.id.mainDisplay)
        })

    }

    override fun onBackPressed() {
        val isBackButtonAvailable = (!supportFragmentManager.findFragmentByTag("myPage")!!.isVisible) &&(!supportFragmentManager.findFragmentByTag("community")!!.isVisible)
        if(isBackButtonAvailable){ // 마이페이지 or 커뮤니티화면 아닐 때만 back버튼 활성화
            super.onBackPressed()
        }
        Log.d("changedPost", supportFragmentManager.findFragmentByTag("1234").toString())
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

    fun changeColorCommunity(){
        mainBottomBarFragment.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.myPageIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }
    fun changeColorMyPage(){
        mainBottomBarFragment.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.communityIcon.setColorFilter(grey, PorterDuff.Mode.SRC_IN)
    }



}