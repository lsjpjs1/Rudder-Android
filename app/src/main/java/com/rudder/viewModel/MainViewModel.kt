package com.rudder.viewModel

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.data.FileInfo
import com.rudder.data.GetCommentInfo
import com.rudder.data.PreviewPost
import com.rudder.data.local.App
import com.rudder.data.remote.*
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
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
    private val _selectedCategoryPosition = MutableLiveData<Int>()
    private val _selectedCommentGroupNum = MutableLiveData<Int>()
    private val _selectedCategoryView = MutableLiveData<View>()
    private val _selectedCategoryNameInAddPost = MutableLiveData<String>()
    private val _selectedPhotoUriList = MutableLiveData<ArrayList<FileInfo>>()
    private val _isAddPostClick = MutableLiveData<Event<Boolean>>()
    private val _isBackClick = MutableLiveData<Event<Boolean>>()
    private val _isScrollBottomTouch = MutableLiveData<Event<Boolean>>()
    private val _isAddPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isLikePost = MutableLiveData<Boolean>()
    private val _commentCountChange = MutableLiveData<Event<Boolean>>()
    private val _commentLikeCountChange = MutableLiveData<Int>()
    private val _isPostMore = MutableLiveData<Event<Boolean>>()
    private val _startLoginActivity = MutableLiveData<Event<Boolean>>()
    private val _postInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _commentInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _photoPickerClickSwitch = MutableLiveData<Boolean?>()

    val photoPickerClickSwitch:LiveData<Boolean?> = _photoPickerClickSwitch
    val commentInnerValueChangeSwitch:LiveData<Boolean> = _commentInnerValueChangeSwitch
    val postInnerValueChangeSwitch:LiveData<Boolean> = _postInnerValueChangeSwitch
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
    val selectedPhotoUriList : LiveData<ArrayList<FileInfo>> = _selectedPhotoUriList


    val isPostMore: LiveData<Event<Boolean>> = _isPostMore
    val startLoginActivity: LiveData<Event<Boolean>> = _startLoginActivity


    val posts: LiveData<ArrayList<PreviewPost>>
        get() = _posts
    val comments: LiveData<ArrayList<Comment>> = _comments
    val categories: LiveData<ArrayList<Category>> = _categories

    init {
        _selectedTab.value = R.id.communityButton
        _selectedCategoryPosition.value = 0
        _postInnerValueChangeSwitch.value=true
        _commentInnerValueChangeSwitch.value=true
        _selectedPhotoUriList.value = arrayListOf()
        _postBody.value = ""
        _categories.value = arrayListOf(
            Category(-1, "All")
        )
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
                arrayListOf()
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
        clearNestedCommentInfo()
        getPosts()
        getCategories()
    }

    suspend fun uploadPhoto(postId:Int){
        GlobalScope.async {
            val list = Repository().getUploadUrls(GetUploadUrlsInfo(getMimeTypeList(),App.prefs.getValue(tokenKey)!!,postId) )
            for(i in 0 until list.size()){
                val file = RequestBody.create(MediaType.parse(_selectedPhotoUriList.value!![i].mimeType),File(_selectedPhotoUriList.value!![i].filePath))
                Repository().uploadImage(file,list[i].asJsonObject.get("url").asString)
            }
            viewModelScope.launch {
                _isAddPostSuccess.value = Event(true)
            }
        }
    }
    fun deletePhotoUriPosition(position: Int){
        val tmpList = _selectedPhotoUriList.value!!
        Log.d("list",tmpList.toString())
        tmpList.removeAt(position)
        _selectedPhotoUriList.value = tmpList
    }

    fun setSelectedPhotoUriList(uriList:ArrayList<FileInfo>){
        val tmpList = _selectedPhotoUriList.value!!
        tmpList.addAll(uriList)
        _selectedPhotoUriList.value = tmpList

    }

    fun getMimeTypeList():ArrayList<String>{
        var mimeTypeList = arrayListOf<String>()
        for(fileInfo in _selectedPhotoUriList.value!!){
            mimeTypeList.add(fileInfo.mimeType)
        }
        return mimeTypeList
    }

    fun onPhotoPickerClick(){
        switch(_photoPickerClickSwitch)
    }

    fun clickNestedCommentReply(groupNum: Int, commentBody: String) {
        _selectedParentCommentBody.value = commentBody
        _selectedCommentGroupNum.value = groupNum
    }

    fun callLoginOut() {
        _startLoginActivity.value = Event(true)

        val key = BuildConfig.TOKEN_KEY
        App.prefs.removeValue(key)
    }



        fun clearNestedCommentInfo() {
            _selectedParentCommentBody.value = ""
            _selectedCommentGroupNum.value = -1
        }

        fun clearAddPost() {
            _selectedPhotoUriList.value = arrayListOf()
            _postBody.value = ""
            _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
            _photoPickerClickSwitch.value = null
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
            _posts.value!![_selectedPostPosition.value!!].isLiked =
                !_posts.value!![_selectedPostPosition.value!!].isLiked
            _isLikePost.value = !(_isLikePost.value)!!//like button 체크 혹은 해제
            _postInnerValueChangeSwitch.value = !_postInnerValueChangeSwitch.value!!
            addLikePost(plusValue)
        }

        fun addLikePost(plusValue: Int) {
            GlobalScope.launch {
                val addLikePostInfo = AddLikePostInfo(
                    _posts.value!![_selectedPostPosition.value!!].postId,
                    App.prefs.getValue(tokenKey)!!,
                    plusValue
                )
                val resJson = Repository().addLikePost(addLikePostInfo)
                viewModelScope.launch {
                    if (resJson.get("isSuccess").asBoolean){
                        _posts.value!![selectedPostPosition.value!!].likeCount = resJson.get("like_count").asInt
                        _postInnerValueChangeSwitch.value = !_postInnerValueChangeSwitch.value!!
                    }
                }

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
            _commentInnerValueChangeSwitch.value = !_commentInnerValueChangeSwitch.value!!
            addLikeComment(plusValue, position)
        }

        fun addLikeComment(plusValue: Int, position: Int) {
            GlobalScope.launch {
                val addLikeCommentInfo = AddLikeCommentInfo(
                    _comments.value!![position].commentId,
                    App.prefs.getValue(tokenKey)!!,
                    plusValue
                )
                val resJson = Repository().addLikeComment(addLikeCommentInfo)
                viewModelScope.launch {
                    if (resJson.get("isSuccess").asBoolean){
                        _comments.value!![position].likeCount = resJson.get("like_count").asInt
                        _commentInnerValueChangeSwitch.value = !_commentInnerValueChangeSwitch.value!!
                    }
                }
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
                }

            }
        }


        fun addPost() {
            GlobalScope.async {
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
                val isSuccess=res.isSuccess
                val postId = res.postId
                if(isSuccess) uploadPhoto(postId)

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

        fun setSelectedCategoryNameInAddPost(position: Int) {
            _selectedCategoryNameInAddPost.value = _categoryNames.value!![position]
        }


        fun clickPostMore() {
            _isPostMore.value = Event(true)
        }

        fun getCategories() {
            GlobalScope.launch {
                var categoryList = Repository().getCategories()
                viewModelScope.launch {
                    _categoryNames.value = splitCategoryNames((categoryList))
                    _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
                    categoryList.add(0, Category(-1, "All"))
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

//        fun isAlreadyReadPost(): Boolean {
//            val alreadyReadPostIdJsonString = App.prefs.getValue(ALREADY_READ_POST_ID_KEY)
//            return if (alreadyReadPostIdJsonString == null || alreadyReadPostIdJsonString == "") { // 조회글 목록 json 자체가 없으면 방금 읽은 postid를 키로 넣고 하나 새로 생성
//                val alreadyReadPostIdJson = JsonObject()
//                alreadyReadPostIdJson.add(
//                    _posts.value!![_selectedPostPosition.value!!].postId.toString(),
//                    JsonNull.INSTANCE
//                )
//                App.prefs.setValue(ALREADY_READ_POST_ID_KEY, alreadyReadPostIdJson.toString())
//                false
//            } else {//조회글 목록 json이 있으면 해당 json에 현재 읽은 postid가 존재하는 지 확인
//                val alreadyReadPostIdJson =
//                    JsonParser().parse(alreadyReadPostIdJsonString).asJsonObject
//                if (alreadyReadPostIdJson.has(_posts.value!![_selectedPostPosition.value!!].postId.toString())) {
//                    true
//                } else {
//                    alreadyReadPostIdJson.add(
//                        _posts.value!![_selectedPostPosition.value!!].postId.toString(),
//                        JsonNull.INSTANCE
//                    )
//                    App.prefs.setValue(ALREADY_READ_POST_ID_KEY, alreadyReadPostIdJson.toString())
//                    false
//                }
//            }
//        }

        fun addPostViewCount() {
            GlobalScope.launch {
                Repository().addPostViewCount(AddPostViewCountInfo(_posts.value!![_selectedPostPosition.value!!].postId))
            }
        }

        fun switch(mutableLiveData: MutableLiveData<Boolean?>){

            mutableLiveData.value = if(mutableLiveData.value==null) true else !mutableLiveData.value!!

        }
    }
