package com.rudder.ui.activity

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.data.Post
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.ui.fragment.*
import com.rudder.viewModel.MainViewModel
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
        supportFragmentManager.beginTransaction()
            .add(R.id.mainBottomBar,mainBottomBarFragment)
            .add(R.id.mainDisplay,communityFragment)
            .commit()


        viewModel.selectedTab.observe(this, Observer {
            Log.d("abc",it.toString()+"    "+R.id.communityButton.toString()+"    "+R.id.myPageButton)
            when(it){
                R.id.communityButton -> {
                    showCommunity()
                    changeColorCommunity()
                    Log.d("whereispost",viewModel.posts.value.toString())
                }
                R.id.myPageButton -> {
                    showMyPage()
                    changeColorMyPage()
                    Log.d("whereispost",viewModel.posts.value.toString())
                }
            }
        })

    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.mainDisplay)!=myPageFragment){
            super.onBackPressed()
        }
    }

    fun showPost(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(communityFragment)
        transaction.addToBackStack(null)
        transaction.add(R.id.mainDisplay,showPostFragment)
        transaction.show(showPostFragment)
        transaction.commit()
    }
    fun showCommunity(){
        val transaction = supportFragmentManager.beginTransaction()
        if(this::myPageFragment.isInitialized){
            transaction.hide(myPageFragment)
            transaction.show(communityFragment)
        }else{
            myPageFragment = MyPageFragment()
            transaction.add(R.id.mainDisplay,myPageFragment)
            transaction.hide(myPageFragment)
            transaction.show(communityFragment)
        }
        transaction.commit()
    }

    fun showMyPage(){
        //supportFragmentManager.beginTransaction().replace(R.id.mainDisplay,myPageFragment).commit()
        val transaction = supportFragmentManager.beginTransaction()
        if(this::myPageFragment.isInitialized){
            transaction.hide(communityFragment)
            transaction.show(myPageFragment)
        }else{
            myPageFragment = MyPageFragment()
            transaction.add(R.id.mainDisplay,myPageFragment)
            transaction.hide(communityFragment)
            transaction.show(myPageFragment)
        }
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