package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.PreviewPost
import com.rudder.data.local.App
import com.rudder.data.remote.AddPostInfo
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Timestamp


class MainViewModel : ViewModel() {

    private var pagingIndex = 0
    private var endPostId = -1
    private val MIN_PROGRESSBAR_TIME = 200.toLong() //millisecond

    val _postBody = MutableLiveData<String>()
    val _commentBody = MutableLiveData<String>()
    private val _selectedTab = MutableLiveData<Int>()
    private val _posts = MutableLiveData<ArrayList<PreviewPost>>()
    private val _comments = MutableLiveData<ArrayList<Comment>>()
    private val _selectedPostPosition = MutableLiveData<Int>()
    private val _isAddPostClick = MutableLiveData<Event<Boolean>>()
    private val _isBackClick = MutableLiveData<Event<Boolean>>()
    private val _isScrollBottomTouch = MutableLiveData<Event<Boolean>>()
    private val _isAddPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isLikeClick = MutableLiveData<Event<Boolean>>()

    private val _islogoutButton = MutableLiveData<Event<Boolean>>()
    private val _startLoginActivity = MutableLiveData<Event<Boolean>>()


    val isLikeClick: LiveData<Event<Boolean>> = _isLikeClick
    val isAddPostSuccess: LiveData<Event<Boolean>> = _isAddPostSuccess
    val isScrollBottomTouch: LiveData<Event<Boolean>> = _isScrollBottomTouch
    val isBackClick: LiveData<Event<Boolean>> = _isBackClick
    val isAddPostClick: LiveData<Event<Boolean>> = _isAddPostClick
    val selectedTab: LiveData<Int> = _selectedTab
    val selectedPostPosition: LiveData<Int> = _selectedPostPosition


    val islogoutButton : LiveData<Event<Boolean>> = _islogoutButton
    val startLoginActivity : LiveData<Event<Boolean>> = _startLoginActivity


    val posts : LiveData<ArrayList<PreviewPost>>
        get() = _posts
    val comments : LiveData<ArrayList<Comment>> = _comments

    init{
        Log.d("call","call")
        _selectedTab.value = R.id.communityButton
        _posts.value=arrayListOf(PreviewPost(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3))
        _comments.value= arrayListOf(Comment("",0,"",Timestamp.valueOf("2021-07-13 11:11:11"),0,"parent",0,0,false))
        _postBody.value=""
        getPosts()
    }


    fun callLoginOut(){
        Log.d("callLoginOut","callLoginOut")
        _startLoginActivity.value = Event(true)

        val key = BuildConfig.TOKEN_KEY
        App.prefs.removeValue(key)
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

    fun clickAddPost(){
        _isAddPostClick.value = Event(true)
    }

    fun clickBack(){
        _isBackClick.value = Event(true)
    }

    fun clickLike(){
        _isLikeClick.value = Event(true)
    }


    fun getPosts(){
        _isScrollBottomTouch.value = Event(true)
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
                _isScrollBottomTouch.value = Event(false)

            }
        }
    }


    fun getComments(){
        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        val getCommentInfo = GetCommentInfo(_posts.value!![_selectedPostPosition.value!!].postId,token!!)
        GlobalScope.launch {
            val resComments=Repository().getComments(getCommentInfo)
            viewModelScope.launch {
                _comments.value = resComments
                Log.d("comment", _comments.value.toString())
            }
        }
    }

    fun addPost(){
        GlobalScope.launch {
            val key = BuildConfig.TOKEN_KEY
            val addPostInfo = AddPostInfo("bulletin","",_postBody.value!!, App.prefs.getValue(key)!!, arrayListOf())
            val res = Repository().addPost(addPostInfo)
            viewModelScope.launch {
                _isAddPostSuccess.value=Event(res)
            }
        }
    }

    fun setSelectedPostPosition(position: Int){
        _selectedPostPosition.value=position
    }


//    fun callLoginOut() { // SEMI!!!
//        Log.d("token", "token")
//        val key = BuildConfig.TOKEN_KEY
//        App.prefs.removeValue(key)
//        var a = App.prefs.getValue(key)
//        Log.d("token123", "$a")
//    }

}