package com.rudder.viewModel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.rudder.R
import com.rudder.data.Post
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.sql.Timestamp


object MainViewModel : ViewModel() {
    private var pagingIndex = 0
    private var endPostId = -1

    private val _selectedTab = MutableLiveData<Int>()
    private val _posts = MutableLiveData<ArrayList<Post>>()
    private val _selectedPostPosition = MutableLiveData<Int>()

    val selectedTab: LiveData<Int> = _selectedTab
    val selectedPostPosition: LiveData<Int> = _selectedPostPosition
    val posts : LiveData<ArrayList<Post>>
        get() = _posts

    init{
        Log.d("call","call")
        _selectedTab.value = R.id.communityButton
        _posts.value=arrayListOf(Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3))
        getPosts()
    }


    fun scrollTouchBottom(){
        pagingIndex += 1
        endPostId = _posts.value!![_posts.value!!.size-1].postId
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
            val resPosts = Repository().getPosts(pagingIndex, endPostId)
            viewModelScope.launch {
                if(_posts.value!!.size == 0){
                    _posts.value = resPosts
                }else{
                    val oldPosts = _posts.value
                    oldPosts!!.addAll(resPosts)
                    Log.d("oldPost",oldPosts.toString())
                    _posts.value= oldPosts!!
                }
            }
        }
    }

    fun setSelectedPostPosition(position: Int){
        _selectedPostPosition.value=position
    }


}