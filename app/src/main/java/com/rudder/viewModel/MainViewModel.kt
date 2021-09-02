package com.rudder.viewModel

import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.*
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.*
import com.rudder.data.local.App
import com.rudder.data.remote.*
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Timestamp


class MainViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val ALREADY_READ_POST_ID_KEY = "alreadyReadPostIdJson"
    private var pagingIndex = 0
    private var endPostId = -1

    val _postBody = MutableLiveData<String>()
    val _commentBody = MutableLiveData<String>()
    val _selectedParentCommentBody = MutableLiveData<String>()
    private val _selectedTab = MutableLiveData<Int>()
    private val _posts = MutableLiveData<ArrayList<PreviewPost>>()
    private val _comments = MutableLiveData<ArrayList<Comment>>()
    private val _categories = MutableLiveData<ArrayList<Category>>()
    private val _categoryNames = MutableLiveData<ArrayList<String>>()
    private val _selectedPostPosition = MutableLiveData<Int>()
    val _selectedCategoryPosition = MutableLiveData<Int>()
    private val _selectedCommentGroupNum = MutableLiveData<Int>()
    private val _selectedCategoryView = MutableLiveData<View>()
    val _selectedCategoryNameInAddPost = MutableLiveData<String>()
    private val _isAddPostClick = MutableLiveData<Event<Boolean>>()
    private val _isBackClick = MutableLiveData<Event<Boolean>>()
    private val _isScrollBottomTouch = MutableLiveData<Event<Boolean>>()
    private val _isAddPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isLikePost = MutableLiveData<Boolean>()
    private val _commentCountChange = MutableLiveData<Event<Boolean>>()
    private val _commentLikeCountChange = MutableLiveData<Int>()

    private val _selectedPostMorePosition = MutableLiveData<Int>()
    private val _selectedCommentMorePosition = MutableLiveData<Int>()

    val _postCategoryInt = MutableLiveData<Int>()


    private val _commentBodyCheck = MutableLiveData<Event<Boolean>>()


    private val _isPostMore = MutableLiveData<Event<Boolean>>()
    private val _isCommentMore = MutableLiveData<Event<Boolean>>()
    private val _isPostMine = MutableLiveData<Event<Boolean>>()
    private val _isCommentMine = MutableLiveData<Event<Boolean>>()

    private val _isPostReport = MutableLiveData<Event<Boolean>>()
    private val _isPostEdit = MutableLiveData<Event<Boolean>>()
    private val _isPostDelete = MutableLiveData<Event<Boolean>>()

    private val _isCommentReport = MutableLiveData<Event<Boolean>>()
    private val _isCommentEdit = MutableLiveData<Event<Boolean>>()
    private val _isCommentDelete = MutableLiveData<Event<Boolean>>()

    private val _commentDeleteComplete = MutableLiveData<Event<Boolean>>()

    private val _startLoginActivity = MutableLiveData<Event<Boolean>>()


    val commentLikeCountChange: LiveData<Int> = _commentLikeCountChange
    val commentCountChange: LiveData<Event<Boolean>> = _commentCountChange
    val isLikePost: LiveData<Boolean> = _isLikePost

    val isAddPostSuccess: LiveData<Event<Boolean>> = _isAddPostSuccess
    val isScrollBottomTouch: LiveData<Event<Boolean>> = _isScrollBottomTouch
    val isBackClick: LiveData<Event<Boolean>> = _isBackClick
    val isAddPostClick: LiveData<Event<Boolean>> = _isAddPostClick
    val selectedTab: LiveData<Int> = _selectedTab
    val categoryNames: LiveData<ArrayList<String>> = _categoryNames
    val selectedPostPosition: LiveData<Int> = _selectedPostPosition
    val selectedCategoryPosition: LiveData<Int> = _selectedCategoryPosition
    val selectedCommentGroupNum: LiveData<Int> = _selectedCommentGroupNum
    val selectedCategoryView: LiveData<View> = _selectedCategoryView
    val selectedCategoryNameInAddPost: LiveData<String> = _selectedCategoryNameInAddPost

    val selectedPostMorePosition: LiveData<Int> = _selectedPostMorePosition

    val selectedCommentMorePosition: LiveData<Int> = _selectedCommentMorePosition
    val postCategoryInt: LiveData<Int> = _postCategoryInt

    val commentBodyCheck: LiveData<Event<Boolean>> = _commentBodyCheck

    val isPostMore: LiveData<Event<Boolean>> = _isPostMore
    val isCommentMore: LiveData<Event<Boolean>> = _isCommentMore
    val isPostMine: LiveData<Event<Boolean>> = _isPostMine
    val isCommentMine: LiveData<Event<Boolean>> = _isCommentMine
    val isPostReport: LiveData<Event<Boolean>> = _isPostReport
    val isPostEdit: LiveData<Event<Boolean>> = _isPostEdit
    val isPostDelete: LiveData<Event<Boolean>> = _isPostDelete
    val isCommentReport: LiveData<Event<Boolean>> = _isCommentReport
    val isCommentEdit: LiveData<Event<Boolean>> = _isCommentEdit
    val isCommentDelete: LiveData<Event<Boolean>> = _isCommentDelete

    val commentDeleteComplete: LiveData<Event<Boolean>> = _commentDeleteComplete





    val startLoginActivity: LiveData<Event<Boolean>> = _startLoginActivity

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
                "",
                0,
                false,
                false
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
                false,
                false
            )
        )
        _postBody.value = ""
        _categories.value = arrayListOf(
            Category(0, "All")
        )
        clearNestedCommentInfo()
        getPosts()
        getCategories()


        _commentDeleteComplete.value = Event(false)
    }


    fun clickNestedCommentReply(groupNum: Int, commentBody: String) {
        _selectedParentCommentBody.value = commentBody
        _selectedCommentGroupNum.value = groupNum
    }

    fun callLoginOut() {
        Log.d("callLoginOut", "callLoginOut")
        _startLoginActivity.value = Event(true)

        val key = BuildConfig.TOKEN_KEY
        App.prefs.removeValue(key)
    }



    fun clearNestedCommentInfo() {
        _selectedParentCommentBody.value = ""
        _selectedCommentGroupNum.value = -1
    }

    fun clearAddPost() {
        _postBody.value = ""
        _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
    }

    fun commentBodyClear() {
        _commentBody.value = ""
    }

    fun scrollTouchBottom() {
        if (_posts.value!!.size > 0) {
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
        _postCategoryInt.value = 0
    }

    fun clickBack() {
        _isBackClick.value = Event(true)
    }

    fun clickPostLike() {

        val plusValue =
            if (_isLikePost.value!!) -1
            else 1
        _posts.value!![_selectedPostPosition.value!!].likeCount =
            _posts.value!![_selectedPostPosition.value!!].likeCount + plusValue
        _isLikePost.value = !(_isLikePost.value)!!//like button 체크 혹은 해제
        addLikePost(plusValue)
    }

    fun addLikePost(plusValue: Int) {
        GlobalScope.launch {
            val addLikePostInfo = AddLikePostInfo(
                _posts.value!![_selectedPostPosition.value!!].postId,
                App.prefs.getValue(tokenKey)!!,
                plusValue
            )
            val res = Repository().addLikePost(addLikePostInfo)

        }
    }

    fun clickCommentLike(position: Int) {
        val plusValue =
            if (_comments.value!![position].isLiked) -1
            else 1
        _comments.value!![position].likeCount =
            _comments.value!![position].likeCount + plusValue
        _comments.value!![position].isLiked = !_comments.value!![position].isLiked
        _commentLikeCountChange.value = position
        addLikeComment(plusValue, position)
    }

    fun addLikeComment(plusValue: Int, position: Int) {
        GlobalScope.launch {
            val addLikeCommentInfo = AddLikeCommentInfo(
                _comments.value!![position].commentId,
                App.prefs.getValue(tokenKey)!!,
                plusValue
            )
            val res = Repository().addLikeComment(addLikeCommentInfo)

        }
    }


    fun getPosts() {
        _isScrollBottomTouch.value = Event(true)
        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        GlobalScope.launch {

            val resPosts = Repository().getPosts(
                pagingIndex,
                endPostId,
                categories.value!![selectedCategoryPosition.value!!].categoryId,
                App.prefs.getValue(tokenKey)!!
            )
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

    fun clearPosts() {
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
        lateinit var addCommentInfo: AddCommentInfo
        _posts.value!![_selectedPostPosition.value!!].commentCount =
            _posts.value!![_selectedPostPosition.value!!].commentCount + 1
        _commentCountChange.value = Event(true)
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
                _commentBody.postValue("")
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
                arrayListOf(),
                _selectedCategoryNameInAddPost.value!!
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


    fun setSelectedCategoryView(view: View) {
        _selectedCategoryView.value = view
    }


    fun clickPostMore(position: Int) {
        _isPostMore.value = Event(true)
        _selectedPostMorePosition.value = position

        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)

        if (_posts.value!![selectedPostMorePosition.value!!].isMine)
            _isPostMine.value = Event(true)
        else
            _isPostMine.value = Event(false)

    }

    fun clickCommentMore(position: Int) {
        _isCommentMore.value = Event(true)
        _selectedCommentMorePosition.value = position
    }


    fun clickPostReport() {
        _isPostReport.value = Event(true)
    }

    fun clickPostEdit() {
        _isPostEdit.value = Event(true)
        _postBody.value = _posts.value!![selectedPostMorePosition.value!!].postBody
        _postCategoryInt.value = _posts.value!![selectedPostMorePosition.value!!].categoryId - 1
    }

    fun clickPostDelete() {
        GlobalScope.launch {
            var result = Repository().deletePostRepository(DeletePostInfo( _posts.value!![selectedPostMorePosition.value!!].postId ))
            _isPostDelete.postValue(Event(result))
        }

    }


    fun clickCommentReport() {
        _isCommentReport.value = Event(true)
    }

    fun clickCommentEdit() {
        Log.d("clickCommentEdit","clickCommentEdit")
        _isCommentEdit.value = Event(true)
        _commentBody.value = _comments.value!![selectedCommentMorePosition.value!!].commentBody
    }


    fun clickCommentDelete() {
        val commentInt = _comments.value!![_selectedCommentMorePosition.value!!].commentId
        val postInt = _posts.value!![_selectedPostPosition.value!!].postId

        GlobalScope.launch {
            var result = Repository().deleteCommentRepository(DeleteCommentInfo(commentInt, postInt))
            _isCommentDelete.postValue(Event(result))

            _commentDeleteComplete.postValue(Event(result))

            if (result) {
                _posts.value!![_selectedPostPosition.value!!].commentCount = _posts.value!![_selectedPostPosition.value!!].commentCount - 1
                _commentCountChange.postValue(Event(result))
                getComments()
            }
        }


    }




    fun getCategories() {
        GlobalScope.launch {
            var categoryList = Repository().getCategories()
            viewModelScope.launch {
                _categoryNames.value = splitCategoryNames((categoryList))
                _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
                categoryList.add(0, Category(0, "All"))
                _categories.value = categoryList
            }
        }
    }

    fun splitCategoryNames(categoryList: ArrayList<Category>): ArrayList<String> {
        var categoryNames = ArrayList<String>()
        for (category in categoryList) {
            categoryNames.add(category.categoryName)
        }
        return categoryNames
    }

    fun isLikePost() {
        GlobalScope.launch {
            val isLikePostInfo = IsLikePostInfo(
                _posts.value!![_selectedPostPosition.value!!].postId,
                App.prefs.getValue(tokenKey)!!
            )
            val res = Repository().isLikePost(isLikePostInfo)
            viewModelScope.launch {
                _isLikePost.value = res
            }
        }
    }

    fun isAlreadyReadPost(): Boolean {
        val alreadyReadPostIdJsonString = App.prefs.getValue(ALREADY_READ_POST_ID_KEY)
        return if (alreadyReadPostIdJsonString == null || alreadyReadPostIdJsonString == "") { // 조회글 목록 json 자체가 없으면 방금 읽은 postid를 키로 넣고 하나 새로 생성
            val alreadyReadPostIdJson = JsonObject()
            alreadyReadPostIdJson.add(
                _posts.value!![_selectedPostPosition.value!!].postId.toString(),
                JsonNull.INSTANCE
            )
            App.prefs.setValue(ALREADY_READ_POST_ID_KEY, alreadyReadPostIdJson.toString())
            false
        } else {//조회글 목록 json이 있으면 해당 json에 현재 읽은 postid가 존재하는 지 확인
            val alreadyReadPostIdJson =
                JsonParser().parse(alreadyReadPostIdJsonString).asJsonObject
            if (alreadyReadPostIdJson.has(_posts.value!![_selectedPostPosition.value!!].postId.toString())) {
                true
            } else {
                alreadyReadPostIdJson.add(
                    _posts.value!![_selectedPostPosition.value!!].postId.toString(),
                    JsonNull.INSTANCE
                )
                App.prefs.setValue(ALREADY_READ_POST_ID_KEY, alreadyReadPostIdJson.toString())
                false
            }
        }
    }

    fun addPostViewCount() {
        GlobalScope.launch {
            Repository().addPostViewCount(AddPostViewCountInfo(_posts.value!![_selectedPostPosition.value!!].postId))
        }
    }


    fun onSelectItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        Log.d("onSelectItem","$pos, $id, ${parent.selectedItem}")
        _selectedCategoryNameInAddPost.value = _categoryNames.value!![pos]
        _postCategoryInt.value = pos - 1
    }


    fun onTextChangeComment() {
        if (_commentBody.value!!.isNotBlank())
            _commentBodyCheck.value = Event(true)
        else
            _commentBodyCheck.value = Event(false)
    }

}
