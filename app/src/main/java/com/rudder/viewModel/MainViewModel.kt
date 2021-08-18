package com.rudder.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.data.GetCommentInfo
import com.rudder.data.Post
import com.rudder.data.PreviewPost
import com.rudder.data.local.App
import com.rudder.data.remote.AddCommentInfo
import com.rudder.data.remote.AddPostInfo
import com.rudder.data.remote.Category
import com.rudder.data.remote.PostApi
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Timestamp


class MainViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private var pagingIndex = 0
    private var endPostId = -1
    private val MIN_PROGRESSBAR_TIME = 200.toLong() //millisecond

    val _postBody = MutableLiveData<String>()
    val _commentBody = MutableLiveData<String>()
    val _selectedParentCommentBody = MutableLiveData<String>()
    private val _selectedTab = MutableLiveData<Int>()
    private val _posts = MutableLiveData<ArrayList<PreviewPost>>()
    private val _comments = MutableLiveData<ArrayList<Comment>>()
    private val _categories = MutableLiveData<ArrayList<Category>>()
    private val _selectedPostPosition = MutableLiveData<Int>()
    private val _selectedCategoryPosition = MutableLiveData<Int>()
    private val _selectedCommentGroupNum = MutableLiveData<Int>()
    private val _selectedCategoryView = MutableLiveData<View>()
    private val _isAddPostClick = MutableLiveData<Event<Boolean>>()
    private val _isBackClick = MutableLiveData<Event<Boolean>>()
    private val _isScrollBottomTouch = MutableLiveData<Event<Boolean>>()
    private val _isAddPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isLikeClick = MutableLiveData<Event<Boolean>>()

    val isLikeClick: LiveData<Event<Boolean>> = _isLikeClick
    val isAddPostSuccess: LiveData<Event<Boolean>> = _isAddPostSuccess
    val isScrollBottomTouch: LiveData<Event<Boolean>> = _isScrollBottomTouch
    val isBackClick: LiveData<Event<Boolean>> = _isBackClick
    val isAddPostClick: LiveData<Event<Boolean>> = _isAddPostClick
    val selectedTab: LiveData<Int> = _selectedTab
    val selectedPostPosition: LiveData<Int> = _selectedPostPosition
    val selectedCategoryPosition: LiveData<Int> = _selectedCategoryPosition
    val selectedCommentGroupNum: LiveData<Int> = _selectedCommentGroupNum
    val selectedCategoryView: LiveData<View> = _selectedCategoryView

    val posts: LiveData<ArrayList<PreviewPost>>
        get() = _posts
    val comments: LiveData<ArrayList<Comment>> = _comments
    val categories: LiveData<ArrayList<Category>> = _categories

    init {
        Log.d("call", "call")
        _selectedTab.value = R.id.communityButton
        _selectedCategoryPosition.value = 0
        _posts.value = arrayListOf(
            PreviewPost(
                1,
                "abc",
                "body",
                "title",
                Timestamp.valueOf("2021-07-13 11:11:11"),
                1,
                2,
                3,
                ""
            )
        )
        _comments.value = arrayListOf(
            Comment(
                "",
                0,
                "",
                Timestamp.valueOf("2021-07-13 11:11:11"),
                0,
                "parent",
                0,
                0,
                false
            )
        )
        _postBody.value = ""
        _categories.value = arrayListOf(
            Category(0,"All")
        )
        clearNestedCommentInfo()
        getPosts()
        getCategories()
    }

    fun clickNestedCommentReply(groupNum: Int, commentBody: String) {
        _selectedParentCommentBody.value = commentBody
        _selectedCommentGroupNum.value = groupNum
    }

    fun clearNestedCommentInfo() {
        _selectedParentCommentBody.value = ""
        _selectedCommentGroupNum.value = -1
    }

    fun clearAddPostBody(){
        _postBody.value = ""
    }

    fun commentBodyClear() {
        _commentBody.value = ""
    }

    fun scrollTouchBottom() {
        if(_posts.value!!.size>0) {
            pagingIndex += 1
            endPostId = _posts.value!![_posts.value!!.size - 1].postId
            getPosts()
        }
    }

    fun clickCommunity() {
        _selectedTab.value = R.id.communityButton
    }

    fun clickMyPage() {
        _selectedTab.value = R.id.myPageButton
    }

    fun clickAddPost() {
        _isAddPostClick.value = Event(true)
    }

    fun clickBack() {
        _isBackClick.value = Event(true)
    }

    fun clickLike() {
        _isLikeClick.value = Event(true)
    }


    fun getPosts() {
        _isScrollBottomTouch.value = Event(true)
        GlobalScope.launch {
            val resPosts = Repository().getPosts(pagingIndex, endPostId,categories.value!![selectedCategoryPosition.value!!].categoryId)
            viewModelScope.launch {
                if (_posts.value!!.size == 0) {
                    _posts.value = resPosts
                } else {
                    val oldPosts = _posts.value
                    oldPosts!!.addAll(resPosts)
                    Log.d("oldPost", oldPosts.toString())
                    _posts.value = oldPosts!!
                }
                _isScrollBottomTouch.value = Event(false)

            }
        }
    }

    fun clearPosts(){
        _posts.value = ArrayList<PreviewPost>()
        pagingIndex = 0
        endPostId = -1
    }

    fun getComments() {
        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        val getCommentInfo =
            GetCommentInfo(_posts.value!![_selectedPostPosition.value!!].postId, token!!)
        GlobalScope.launch {
            val resComments = Repository().getComments(getCommentInfo)
            viewModelScope.launch {
                _comments.value = resComments
                Log.d("comment", _comments.value.toString())

            }
        }
    }

    fun addComment() {
        lateinit var addCommentInfo : AddCommentInfo
        GlobalScope.launch {
            if (_selectedCommentGroupNum.value == -1) { // _selectedCommentGroupNum.value==-1 -> parent인 댓글
                addCommentInfo = AddCommentInfo(
                    _posts.value!![_selectedPostPosition.value!!].postId,
                    _commentBody.value!!,
                    App.prefs.getValue(tokenKey)!!,
                    "parent",
                    -1
                )
            } else {
                addCommentInfo = AddCommentInfo(
                    _posts.value!![_selectedPostPosition.value!!].postId,
                    _commentBody.value!!,
                    App.prefs.getValue(tokenKey)!!,
                    "child",
                    _selectedCommentGroupNum.value!!
                )
            }
            val isSuccess = Repository().addComment(addCommentInfo)
            if (isSuccess) {
                getComments()
            }

        }
    }


    fun addPost() {
        GlobalScope.launch {
            val key = BuildConfig.TOKEN_KEY
            val addPostInfo = AddPostInfo(
                "bulletin",
                "",
                _postBody.value!!,
                App.prefs.getValue(key)!!,
                arrayListOf()
            )
            val res = Repository().addPost(addPostInfo)
            viewModelScope.launch {
                _isAddPostSuccess.value = Event(res)
            }
        }
    }

    fun setSelectedPostPosition(position: Int) {
        _selectedPostPosition.value = position
    }

    fun setSelectedCategoryPosition(position: Int) {
        _selectedCategoryPosition.value = position
    }

    fun setSelectedCategoryView(view : View) {
        _selectedCategoryView.value = view
    }


    fun callLoginOut() { // SEMI!!!
        Log.d("token", "token")
        val key = BuildConfig.TOKEN_KEY
        App.prefs.removeValue(key)
        var a = App.prefs.getValue(key)
        Log.d("token123", "$a")
    }

    fun getCategories() {
        GlobalScope.launch {
            var categoryList = Repository().getCategories()
            categoryList.add(0, Category(0,"All"))

            viewModelScope.launch {
                _categories.value = categoryList
            }
        }
    }

}