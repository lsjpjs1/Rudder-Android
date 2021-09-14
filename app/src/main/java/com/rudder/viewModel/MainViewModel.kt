package com.rudder.viewModel


import android.content.ContentValues
import android.net.Uri
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
import com.rudder.data.Comment
import com.rudder.data.FileInfo
import com.rudder.data.GetCommentInfo
import com.rudder.data.PreviewPost
import com.rudder.data.local.App
import com.rudder.data.remote.*
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.sql.Timestamp
import com.rudder.util.FileUtil


class MainViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val ALREADY_READ_POST_ID_KEY = "alreadyReadPostIdJson"
    private var pagingIndex = 0
    private var endPostId = -1

    val _postEditBody = MutableLiveData<String>()
    val _commentEditBody = MutableLiveData<String>()
    val _postId = MutableLiveData<Int>()
    val _postBody = MutableLiveData<String>()
    val _commentBody = MutableLiveData<String>()
    val _selectedParentCommentBody = MutableLiveData<String>()
    private val _selectedTab = MutableLiveData<Int>()
    private var _posts = MutableLiveData<ArrayList<PreviewPost>>()
    private val _comments = MutableLiveData<ArrayList<Comment>>()
    private val _categories = MutableLiveData<ArrayList<Category>>()
    private val _categoryNames = MutableLiveData<ArrayList<String>>()
    private val _selectedPostPosition = MutableLiveData<Int>()
    val _selectedCategoryPosition = MutableLiveData<Int>()
    private val _selectedCommentGroupNum = MutableLiveData<Int>()
    private val _selectedCategoryView = MutableLiveData<View>()
    val _selectedCategoryNameInAddPost = MutableLiveData<String>()

    private val _selectedPhotoUriList = MutableLiveData<ArrayList<FileInfo>>()
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
    private val _isPostEdit = MutableLiveData<Boolean?>()
    private val _isPostDelete = MutableLiveData<Event<Boolean>>()

    private val _isCommentReport = MutableLiveData<Event<Boolean>>()
    private val _isCommentEdit = MutableLiveData<Event<Boolean>>()
    private val _isCommentDelete = MutableLiveData<Event<Boolean>>()

    private val _isDeleteCommentSuccess = MutableLiveData<Event<Boolean>>()
    private val _isEditCommentSuccess = MutableLiveData<Event<Boolean>>()
    private val _isReportCommentSuccess = MutableLiveData<Event<Boolean>>()

    private val _isEditPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isReportPostSuccess = MutableLiveData<Event<Boolean>>()

    val _reportPostBody = MutableLiveData<String>()
    val _reportCommentBody = MutableLiveData<String>()

    private val _isCancelClick = MutableLiveData<Event<Boolean>>()

    private val _startLoginActivity = MutableLiveData<Event<Boolean>>()
    private val _postInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _commentInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _photoPickerClickSwitch = MutableLiveData<Boolean?>()


    private val _imageCount = MutableLiveData<Int>()

    private val _myProfileImageUrl = MutableLiveData<String>()
//    private val _noticeResponse = MutableLiveData<NoticeResponse>()
//
//    val noticeResponse:LiveData<NoticeResponse> = _noticeResponse
    val myProfileImageUrl:LiveData<String> = _myProfileImageUrl

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

    val selectedPostMorePosition: LiveData<Int> = _selectedPostMorePosition

    val selectedCommentMorePosition: LiveData<Int> = _selectedCommentMorePosition
    val postCategoryInt: LiveData<Int> = _postCategoryInt

    val commentBodyCheck: LiveData<Event<Boolean>> = _commentBodyCheck

    val isPostMore: LiveData<Event<Boolean>> = _isPostMore
    val isCommentMore: LiveData<Event<Boolean>> = _isCommentMore
    val isPostMine: LiveData<Event<Boolean>> = _isPostMine
    val isCommentMine: LiveData<Event<Boolean>> = _isCommentMine
    val isPostReport: LiveData<Event<Boolean>> = _isPostReport
    val isPostEdit: LiveData<Boolean?> = _isPostEdit
    val isPostDelete: LiveData<Event<Boolean>> = _isPostDelete
    val isCommentReport: LiveData<Event<Boolean>> = _isCommentReport
    val isCommentEdit: LiveData<Event<Boolean>> = _isCommentEdit
    val isCommentDelete: LiveData<Event<Boolean>> = _isCommentDelete


    val isDeleteCommentSuccess: LiveData<Event<Boolean>> = _isDeleteCommentSuccess
    val isEditCommentSuccess: LiveData<Event<Boolean>> = _isEditCommentSuccess
    val isReportCommentSuccess: LiveData<Event<Boolean>> = _isReportCommentSuccess


    val isEditPostSuccess : LiveData<Event<Boolean>> = _isEditPostSuccess
    val isReportPostSuccess : LiveData<Event<Boolean>> = _isReportPostSuccess

    val reportPostBody: LiveData<String> = _reportPostBody
    val reportCommentBody: LiveData<String> = _reportCommentBody

    val isCancelClick : LiveData<Event<Boolean>> = _isCancelClick

    val startLoginActivity: LiveData<Event<Boolean>> = _startLoginActivity

    var posts: LiveData<ArrayList<PreviewPost>> = _posts
    val comments: LiveData<ArrayList<Comment>> = _comments
    val categories: LiveData<ArrayList<Category>> = _categories

    val imageCount: LiveData<Int> = _imageCount


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
                false,
                arrayListOf(),
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
                false,
                false,
                ""
            )
        )
        _postBody.value = ""
        _categories.value = arrayListOf(
            Category(-1, "All")
        )

        clearNestedCommentInfo()
        getPosts()
        getCategories()
        getSelectedCategories()


        _isDeleteCommentSuccess.value = Event(false)
        _imageCount.value = 0
    }
        fun getMyProfileImageUrl(){
        GlobalScope.async {
            val url = Repository().getMyProfileImageUrl(MyProfileImageRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!)).url
            viewModelScope.launch{
                _myProfileImageUrl.value = url
                Log.d("myprofile","call123")
            }
        }
    }

//    fun getNotice(){
//        GlobalScope.async {
//            val version = BuildConfig.VERSION_NAME
//            val response = Repository().getNotice(NoticeRequest("android",version))
//            viewModelScope.launch{
//                _noticeResponse.value = response
//            }
//        }
//    }

    suspend fun uploadPhoto(postId:Int){
        GlobalScope.async {
            val list = Repository().getUploadUrls(GetUploadUrlsInfo(getMimeTypeList(),App.prefs.getValue(tokenKey)!!,postId) )
            for(i in 0 until list.size){
                val file = RequestBody.create(MediaType.parse(_selectedPhotoUriList.value!![i].mimeType),
                    FileUtil.getDownsizedImageBytes(_selectedPhotoUriList.value!![i].filePath))
                Repository().uploadImage(file,list[i])
            }
            viewModelScope.launch {
                _isAddPostSuccess.value = Event(true)
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
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
//        _selectedParentCommentBody.value = ""
//        _selectedCommentGroupNum.value = -1
        _selectedParentCommentBody.postValue( "" )
        _selectedCommentGroupNum.postValue( -1 )
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

    fun scrollTouchTop() {
        clearPosts()
        getPosts()
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


    fun getPosts() {
        _isScrollBottomTouch.value = Event(true)
        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        Log.d("progressbar_getPost","progressbar_getPost")

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

    fun addLikeComment(plusValue: Int, position: Int) {
        GlobalScope.launch {
            val addLikeCommentInfo = AddLikeCommentInfo(
                _comments.value!![position].commentId,
                App.prefs.getValue(tokenKey)!!,
                plusValue
            )
            val resJson = Repository().addLikeComment(addLikeCommentInfo)
            viewModelScope.launch {
                if (resJson.get("isSuccess").asBoolean) {
                    _comments.value!![position].likeCount = resJson.get("like_count").asInt
                    _commentInnerValueChangeSwitch.value =
                        !_commentInnerValueChangeSwitch.value!!
                }
            }
        }
    }

    fun clearComments() {
        _comments.postValue(ArrayList<Comment>())
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

            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val resComments = Repository().getComments(getCommentInfo)
            viewModelScope.launch {
                var tmpCommentList = ArrayList<Comment>()
                for(idx in resComments.indices) {
                    Log.d("for_comment", "${resComments[idx]}")
                    if (idx == 0){
                        if(resComments[idx].status == "child" ) { // 그 패턴이면
                            tmpCommentList.add(
                                Comment("-", 0, "* Deleted Comment", Timestamp.valueOf("2021-07-13 11:11:11"), 0, "parent", 0, resComments[idx].groupNum, false, false,"" ) //dummyComment
                            )
                            tmpCommentList.add(resComments[idx])
                        }
                        else
                            tmpCommentList.add(resComments[idx])
                    } else {
                        if(resComments[idx].status == "child" && resComments[idx].groupNum != resComments[idx - 1].groupNum ) { // 그 패턴이면
                            tmpCommentList.add(
                                Comment("-", 0, "* Deleted Comment", Timestamp.valueOf("2021-07-13 11:11:11"), 0, "parent", 0, resComments[idx].groupNum, false, false,"") // dummyComment
                            )
                            tmpCommentList.add(resComments[idx])
                        }
                        else
                            tmpCommentList.add(resComments[idx])

                    }
                }
                _comments.value = tmpCommentList
                Log.d("comment", _comments.value.toString())

            }
            ProgressBarUtil._progressBarFlag.postValue(Event(false))
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
                clearNestedCommentInfo()
            }
        }
    }


    fun addPost() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val tmpCategoryId = _postCategoryInt.value!! + 1
            val key = BuildConfig.TOKEN_KEY
            val addPostInfo = AddPostInfo(
                "bulletin",
                "",
                _postBody.value!!,
                App.prefs.getValue(key)!!,
                arrayListOf(),
                tmpCategoryId
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


    fun clickPostMore(position: Int) {
        _isPostMore.value = Event(true)
        _selectedPostMorePosition.value = position
        _postId.value = _posts.value!![_selectedPostMorePosition.value!!].postId


        if (_posts.value!![selectedPostMorePosition.value!!].isMine)
            _isPostMine.value = Event(true)
        else
            _isPostMine.value = Event(false)

    }

    fun clickCommentMore(position: Int) {
        _isCommentMore.value = Event(true)
        _selectedCommentMorePosition.value = position

        if (_comments.value!![_selectedCommentMorePosition.value!!].isMine)
            _isCommentMine.value = Event(true)
        else
            _isCommentMine.value = Event(false)
    }


    fun clickPostReport() {
        _isPostReport.value = Event(true)
        _reportPostBody.value = ""
    }

    fun clickPostEdit() {
        switch(_isPostEdit)
        _postBody.value = _posts.value!![selectedPostMorePosition.value!!].postBody
        _postCategoryInt.value = _posts.value!![selectedPostMorePosition.value!!].categoryId - 1
    }

    fun clickPostDelete() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            var result = Repository().deletePostRepository(DeletePostInfo( _posts.value!![selectedPostMorePosition.value!!].postId ))
            _isPostDelete.postValue(Event(result))
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }

    }


    fun clickCommentReport() {
        _isCommentReport.value = Event(true)
        _reportCommentBody.value = ""
    }

    fun clickCommentEdit() {
        _isCommentEdit.value = Event(true)
        _commentEditBody.value = _comments.value!![selectedCommentMorePosition.value!!].commentBody
        _commentBody.value = ""
    }


    fun clickCommentDelete() {
        val commentInt = _comments.value!![_selectedCommentMorePosition.value!!].commentId
        val postInt = _posts.value!![_selectedPostPosition.value!!].postId

        GlobalScope.launch {
            var result = Repository().deleteCommentRepository(DeleteCommentInfo(commentInt, postInt))
            _isCommentDelete.postValue(Event(result))
            _isDeleteCommentSuccess.postValue(Event(result))

            if (result) {
                _posts.value!![_selectedPostPosition.value!!].commentCount = _posts.value!![_selectedPostPosition.value!!].commentCount - 1
                _commentCountChange.postValue(Event(result))
                getComments()

            }
        }
    }


    fun getSelectedCategories() { // selected 된 것 불러오기
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)

        GlobalScope.launch {
            var categoryList = Repository().getSelectedCategoriesRepository( Token(App.prefs.getValue(tokenKey)!!) )
            viewModelScope.launch {
                categoryList.add(0, Category(-1, "All"))
                _categories.value = categoryList

            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun getCategories() {
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)

        GlobalScope.launch {
            var categoryList = Repository().getCategories()
            viewModelScope.launch {
                _categoryNames.value = splitCategoryNames(categoryList)
                _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
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


    fun onTextChangeComment() {
        if (_commentBody.value!!.isNotBlank())
            _commentBodyCheck.value = Event(true)
        else
            _commentBodyCheck.value = Event(false)
    }


    fun switch(mutableLiveData: MutableLiveData<Boolean?>){
        mutableLiveData.value = if(mutableLiveData.value==null) true else !mutableLiveData.value!!
    }


    fun editPost() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val key = BuildConfig.TOKEN_KEY
            val editPostInfo = EditPostInfo( _postBody.value!!, _postId.value!!, App.prefs.getValue(key)!! )
            val result = Repository().editPostRepository(editPostInfo)

            Log.d("editpost123", "${selectedPostMorePosition.value},${selectedPostPosition.value} ")
            viewModelScope.launch {
                _isEditPostSuccess.value = Event(result)
                clearPosts()
                getPosts()
            }

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }



    fun editComment() {
        GlobalScope.launch {
            val commentInt = _comments.value!![_selectedCommentMorePosition.value!!].commentId
            val editCommentInfo = EditCommentInfo( _commentEditBody.value!!, commentInt, App.prefs.getValue(tokenKey)!! )
            val result = Repository().editCommentRepository(editCommentInfo)
            _isEditCommentSuccess.postValue(Event(result))

            if (result){
                clearComments()
                getComments()
                _commentBody.postValue("")
            }
        }
    }

    fun reportComment() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

            val reportInfo = ReportInfo( App.prefs.getValue(tokenKey)!!, _comments.value!![_selectedCommentMorePosition.value!!].commentId, _reportCommentBody.value!! ,"comment")
            val result = Repository().reportRepository(reportInfo)
            _isReportCommentSuccess.postValue(Event(result))

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }



    fun reportPost() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

            val reportInfo = ReportInfo( App.prefs.getValue(tokenKey)!!, _postId.value!!, _reportPostBody.value!! ,"post")
            val result = Repository().reportRepository(reportInfo)
            _isReportPostSuccess.postValue(Event(result))

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clickCancel() {
        _isCancelClick.value = Event(true)
        _commentBody.value = ""
        _reportPostBody.value = ""
        _reportCommentBody.value = ""
    }


    fun imageSizeCount(position: Int) {
        _imageCount.value = _posts.value!![position].imageUrls.size
    }



}
