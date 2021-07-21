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

//    fun hideOtherFragments(fragment: Fragment, id: Int){
//        val transaction = supportFragmentManager.beginTransaction()
//        val otherFragments = findFragmentsInId(id)
//        for(frag in otherFragments){
//            if(frag.isVisible) transaction.hide(frag)
//        }
//        transaction.show(fragment)
//
//    }
//    fun findFragmentsInId(id: Int) : ArrayList<Fragment>{
//        var resFragments: ArrayList<Fragment> = arrayListOf()
//        for(fragment in supportFragmentManager.fragments){
//            if(fragment.id == id){
//                resFragments.add(fragment)
//            }
//        }
//        return resFragments
//    }

    fun showPost(){
        FragmentShowHide(supportFragmentManager).showFragmentForShowPost(showPostFragment,R.id.mainDisplay,"showPost")
    }

//    fun showCommunity(){
//        val transaction = supportFragmentManager.beginTransaction()
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainDisplay)!!
//        if(this::myPageFragment.isInitialized){
//            transaction.hide(currentFragment)
//            transaction.show(communityFragment)
//        }else{
//            myPageFragment = MyPageFragment()
//            transaction.add(R.id.mainDisplay,myPageFragment)
//            transaction.hide(currentFragment)
//            transaction.show(communityFragment)
//        }
//        transaction.commit()
//    }
    fun showCommunity(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(myPageFragment)
        transaction.show(communityFragment)
        transaction.commit()
    }

//    fun showMyPage(){
//        val transaction = supportFragmentManager.beginTransaction()
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainDisplay)!!
//        if(this::myPageFragment.isInitialized){
//            transaction.hide(currentFragment)
//            transaction.show(myPageFragment)
//        }else{
//            myPageFragment = MyPageFragment()
//            transaction.add(R.id.mainDisplay,myPageFragment)
//            transaction.hide(currentFragment)
//            transaction.show(myPageFragment)
//        }
//        transaction.commit()
//
//    }

    fun showMyPage(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(communityFragment)
        transaction.show(myPageFragment)
        transaction.commit()

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