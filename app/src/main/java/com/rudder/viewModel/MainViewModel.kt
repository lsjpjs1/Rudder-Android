package com.rudder.viewModel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.R
import com.rudder.data.Post
import com.rudder.data.repository.Repository
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Timestamp


object MainViewModel : ViewModel() {
    private val _selectedTab = MutableLiveData<Int>()
    private val _posts = MutableLiveData<ArrayList<Post>>()

    val selectedTab: LiveData<Int> = _selectedTab
    val posts : LiveData<ArrayList<Post>>
        get() = _posts

    init{
        _selectedTab.value = R.id.communityButton
        _posts.value=arrayListOf(Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3))
        getPosts()

    }

    fun clickCommunity(){
        _selectedTab.value = R.id.communityButton
    }

    fun clickMyPage(){
        _selectedTab.value = R.id.myPageButton
    }

    fun getPosts(){
        GlobalScope.launch {
            val resPosts = Repository().getPosts()
            viewModelScope.launch {
                _posts.value = resPosts
            }
        }
    }

}