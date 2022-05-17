package com.rudder.viewModel


import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.collection.ArraySet
import androidx.lifecycle.*
import com.rudder.BuildConfig
import com.rudder.data.*
import com.rudder.data.local.App
import com.rudder.data.remote.*
import com.rudder.data.repository.Repository
import com.rudder.data.repository.RepositoryNotice
import com.rudder.util.Event
import com.rudder.util.FileUtil
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.sql.Timestamp


open class MainViewModel : ViewModel(), ViewModelInterface {
    enum class PostMode{
        NORMAL,SEARCH
    }
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val ALREADY_READ_POST_ID_KEY = "alreadyReadPostIdJson"
    var pagingIndex = 0
    var endPostId = -1

    val repository = Repository()
    val repositoryNotice = RepositoryNotice()


    val _isShowPostRefreshSuccess = MutableLiveData<Event<Boolean>>()
    val _toastMessage = MutableLiveData<String?>()
    val _isPostFromId = MutableLiveData<Event<Boolean>>()
    val _postFromId = MutableLiveData<PreviewPost?>()
    val _postEditBody = MutableLiveData<String>()
    val _commentEditBody = MutableLiveData<String>()
    val _postId = MutableLiveData<Int>()
    val _postBody = MutableLiveData<String>()
    val _commentBody = MutableLiveData<String>()
    private val _selectedParentCommentBody = MutableLiveData<String>()
    var _posts = MutableLiveData<ArrayList<PreviewPost>>()
    private val _comments = MutableLiveData<ArrayList<Comment>>()
    private val _allCategories = MutableLiveData<ArrayList<Category>>()
    private val _allClubCategories = MutableLiveData<ArrayList<Category>>()
    private val _userSelectCategories = MutableLiveData<ArrayList<Category>>()
    private val _categoryNames = MutableLiveData<ArrayList<String>>()
    private val _selectedPostPosition = MutableLiveData<Int>()
    val _selectedCategoryPosition = MutableLiveData<Int>()
    private val _selectedCommentGroupNum = MutableLiveData<Int>()
    private val _selectedCategoryView = MutableLiveData<View>()
    val _selectedCategoryNameInAddPost = MutableLiveData<String>()
    private val _selectedRequestJoinClubCategoryId = MutableLiveData<Int>()
    private val _selectedPhotoUriList = MutableLiveData<ArrayList<FileInfo>>()
    private val _isScrollBottomTouch = MutableLiveData<Event<Boolean>>()
    private val _isAddPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isLikePost = MutableLiveData<Boolean>()
    private val _commentCountChange = MutableLiveData<Event<Boolean>>()
    private val _commentLikeCountChange = MutableLiveData<Int>()
    private val _selectedPostMorePosition = MutableLiveData<Int>()
    private val _selectedCommentMorePosition = MutableLiveData<Int>()
    val _postCategoryInt = MutableLiveData<Int>()
    val _departmentCategoryAInt = MutableLiveData<Int?>()
    val _departmentCategoryBInt = MutableLiveData<Int?>()
    private val _commentBodyCheck = MutableLiveData<Event<Boolean>>()
    private val _isPostMore = MutableLiveData<Event<Boolean>>()
    private val _isPostMorePreventDouble = MutableLiveData<Boolean?>()
    private val _isCommentMorePreventDouble = MutableLiveData<Boolean?>()
    private val _isCommentMore = MutableLiveData<Event<Boolean>>()
    private val _isPostMine = MutableLiveData<Event<Boolean>>()
    private val _isCommentMine = MutableLiveData<Event<Boolean>>()
    private val _isPostReport = MutableLiveData<Event<Boolean>>()
    private val _isPostEdit = MutableLiveData<Boolean?>()
    private val _isPostDelete = MutableLiveData<Event<Boolean>>()
    private val _isPostDeleteShowPost = MutableLiveData<Event<Boolean>>()
    private val _isBlockUser = MutableLiveData<Event<Boolean>>()
    private val _isBlockUserInComment = MutableLiveData<Event<Boolean>>()
    private val _isCommentReport = MutableLiveData<Event<Boolean>>()
    private val _isContactUs = MutableLiveData<Event<Boolean>>()
    private val _isCommentEdit = MutableLiveData<Event<Boolean>>()
    private val _isClubJoinRequest = MutableLiveData<Event<Boolean>>()
    private val _isCommentDelete = MutableLiveData<Event<Boolean>>()
    private val _isDeleteCommentSuccess = MutableLiveData<Event<Boolean>>()
    private val _isEditCommentSuccess = MutableLiveData<Event<Boolean>>()
    private val _isReportCommentSuccess = MutableLiveData<Event<Boolean>>()
    val _isEditPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isReportPostSuccess = MutableLiveData<Event<Boolean>>()
    private val _isContactUsSuccess = MutableLiveData<Event<Boolean>>()
    val _reportPostBody = MutableLiveData<String>()
    val _userRequestBody = MutableLiveData<String>()
    val _reportCommentBody = MutableLiveData<String>()
    val _clubJoinRequestBody = MutableLiveData<String>()
    private val _isCancelClick = MutableLiveData<Event<Boolean>>()
    private val _isReportDialogCancel = MutableLiveData<Event<Boolean>>()
    private val _isCommentReportDialogCancel = MutableLiveData<Event<Boolean>>()
    private val _isCommentEditDialogCancel = MutableLiveData<Event<Boolean>>()
    private val _startLoginActivity = MutableLiveData<Event<Boolean>>()
    private val _postInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _commentInnerValueChangeSwitch = MutableLiveData<Boolean>()
    private val _photoPickerClickSwitch = MutableLiveData<Event<Boolean?>>()
    private val _imageCount = MutableLiveData<Int>()
    private val _myProfileImageUrl = MutableLiveData<String>()
    var noticeAlreadyShow = false
    private val _noticeResponse = MutableLiveData<String>()
    private val _commonCategoryList = MutableLiveData<ArrayList<Category>>()
    private val _departmentACategoryList = MutableLiveData<ArrayList<Category>>()
    private val _departmentBCategoryList = MutableLiveData<ArrayList<Category>>()


    var isShowPostRefreshSuccess: LiveData<Event<Boolean>> = _isShowPostRefreshSuccess
    var isCommentReportDialogCancel: LiveData<Event<Boolean>> = _isCommentReportDialogCancel
    var isReportDialogCancel: LiveData<Event<Boolean>> = _isReportDialogCancel
    var isCommentEditDialogCancel: LiveData<Event<Boolean>> = _isCommentEditDialogCancel
    var _categoryIdSelectList = MutableLiveData<ArraySet<Int>>()
    var _categoryIdAllList = MutableLiveData<ArrayList<Int>>()
    val _categorySelectApply = MutableLiveData<Event<Boolean>>()
    val _categoryNamesForSelection = MutableLiveData<ArrayList<String>>()
    val _isStringBlank = MutableLiveData<Event<Boolean>>()
    val _isUnvalidCategorySelect = MutableLiveData<Event<Boolean>>()
    private val _searchPosts = MutableLiveData<ArrayList<PreviewPost>>()
    val toastMessage : LiveData<String?> = _toastMessage
    val commentBody : LiveData<String> = _commentBody
    val isPostFromId: LiveData<Event<Boolean>> = _isPostFromId
    val postFromId: LiveData<PreviewPost?> = _postFromId
    val searchPosts: LiveData<ArrayList<PreviewPost>> = _searchPosts
    val selectedRequestJoinClubCategoryId:LiveData<Int> = _selectedRequestJoinClubCategoryId
    val noticeResponse:LiveData<String> = _noticeResponse
    val myProfileImageUrl:LiveData<String> = _myProfileImageUrl
    val photoPickerClickSwitch:LiveData<Event<Boolean?>> = _photoPickerClickSwitch
    val commentInnerValueChangeSwitch:LiveData<Boolean> = _commentInnerValueChangeSwitch
    val postInnerValueChangeSwitch:LiveData<Boolean> = _postInnerValueChangeSwitch
    val commentLikeCountChange: LiveData<Int> = _commentLikeCountChange
    val commentCountChange: LiveData<Event<Boolean>> = _commentCountChange
    val isLikePost: LiveData<Boolean> = _isLikePost
    val isAddPostSuccess: LiveData<Event<Boolean>> = _isAddPostSuccess
    val isScrollBottomTouch: LiveData<Event<Boolean>> = _isScrollBottomTouch
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
    val departmentCategoryAInt: LiveData<Int?> = _departmentCategoryAInt
    val departmentCategoryBInt: LiveData<Int?> = _departmentCategoryBInt
    val commentBodyCheck: LiveData<Event<Boolean>> = _commentBodyCheck
    val isPostMore: LiveData<Event<Boolean>> = _isPostMore
    val isPostMorePreventDouble: LiveData<Boolean?> = _isPostMorePreventDouble
    val isCommentMorePreventDouble: LiveData<Boolean?> = _isCommentMorePreventDouble
    val isCommentMore: LiveData<Event<Boolean>> = _isCommentMore
    val isPostMine: LiveData<Event<Boolean>> = _isPostMine
    val isCommentMine: LiveData<Event<Boolean>> = _isCommentMine
    val isPostReport: LiveData<Event<Boolean>> = _isPostReport
    val isPostEdit: LiveData<Boolean?> = _isPostEdit
    val isPostDelete: LiveData<Event<Boolean>> = _isPostDelete
    val isPostDeleteShowPost: LiveData<Event<Boolean>> = _isPostDeleteShowPost
    val isBlockUser: LiveData<Event<Boolean>> = _isBlockUser
    val isBlockUserInComment: LiveData<Event<Boolean>> = _isBlockUserInComment
    val isCommentReport: LiveData<Event<Boolean>> = _isCommentReport
    val isCommentEdit: LiveData<Event<Boolean>> = _isCommentEdit
    val isClubJoinRequest: LiveData<Event<Boolean>> = _isClubJoinRequest
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
    val userSelectCategories: LiveData<ArrayList<Category>> = _userSelectCategories
    val allCategories: LiveData<ArrayList<Category>> = _allCategories
    val allClubCategories: LiveData<ArrayList<Category>> = _allClubCategories
    val imageCount: LiveData<Int> = _imageCount
    var categoryIdSelectList: LiveData<ArraySet<Int>> = _categoryIdSelectList
    var categoryIdAllList: LiveData<ArrayList<Int>> = _categoryIdAllList
    val categorySelectApply: LiveData<Event<Boolean>> = _categorySelectApply // Apply button
    val categoryNamesForSelection: LiveData<ArrayList<String>> = _categoryNamesForSelection
    val selectedParentCommentBody: LiveData<String> = _selectedParentCommentBody
    val isStringBlank : LiveData<Event<Boolean>> = _isStringBlank
    val isUnvalidCategorySelect : LiveData<Event<Boolean>> = _isUnvalidCategorySelect
    var postMode : PostMode = PostMode.NORMAL
    val commonCategoryList: LiveData<ArrayList<Category>> = _commonCategoryList
    val departmentACategoryList: LiveData<ArrayList<Category>> = _departmentACategoryList
    val departmentBCategoryList: LiveData<ArrayList<Category>> = _departmentBCategoryList


    init {
        _selectedPostPosition.value = 0
        _selectedCategoryPosition.value = 0
        _postInnerValueChangeSwitch.value=true
        _commentInnerValueChangeSwitch.value=true
        _selectedPhotoUriList.value = arrayListOf()
        _postBody.value = ""
        _allCategories.value = arrayListOf()
        _allClubCategories.value = arrayListOf()
        _userSelectCategories.value = arrayListOf(
            Category(-1, "All","t","common","All")
        )
        _posts.value = arrayListOf(
            PreviewPost(
                1,
                "abc",
                0,
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
                "",
                categoryAbbreviation = ""
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
                "",
                -1)
        )
        _postBody.value = ""

        clearNestedCommentInfo()

        viewModelScope.async {
            getCategories().await()
            getClubCategories()
        }

        getSelectedCategories()
        _isDeleteCommentSuccess.value = Event(false)
        _imageCount.value = 0
        _categoryIdSelectList.value = ArraySet<Int>()
        _categoryIdAllList.value = ArrayList<Int>()
        _categoryNamesForSelection.value = ArrayList<String>()
        _commonCategoryList.value = ArrayList<Category>()
        _departmentACategoryList.value = ArrayList<Category>()
        _departmentBCategoryList.value = ArrayList<Category>()


    }

    fun setMyProfileImageUrl(url:String){
        _myProfileImageUrl.value = url
    }


    fun requestJoinClub() { // 동아리 카테고리 불러오기
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)
        GlobalScope.launch {
            val isSuccess = repository.requestJoinClub(RequestJoinClubRequest(App.prefs.getValue(tokenKey)!!,_selectedRequestJoinClubCategoryId.value!!,_clubJoinRequestBody.value!!))
            viewModelScope.launch {
                if(isSuccess){
                    getClubCategories()
                    clickDialogCancel()
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }

    }


    fun setSelectedRequestJoinClubCategoryId(id: Int){
        _selectedRequestJoinClubCategoryId.value = id
    }


    fun findCategoryIndexById(id: Int) : Int {
        var index = 0
        val tmpList: ArrayList<Category> = arrayListOf()
        tmpList.addAll(_userSelectCategories.value!!)
        tmpList.addAll(_allClubCategories.value!!)
        tmpList.addAll(_allCategories.value!!)
        for(i in 0 until tmpList.size) {
            if (tmpList[i].categoryId == id){
                index = i
            }

        }
        return index
    }


    fun getClubCategories() { // 동아리 카테고리 불러오기
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)
        GlobalScope.launch {
            var categoryList = repository.getClubCategories(GetCategoriesRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY),null))
            viewModelScope.launch {
                _allClubCategories.value= arrayListOf()
                val tmp = _allClubCategories.value
                tmp?.addAll(categoryList)
                _allClubCategories.value=tmp!!
                for(category in categoryList){
                    if(category.isMember=="t"){
                        _categoryNames.value?.let{
                            if(category.categoryName !in _categoryNames.value!!) {
                                var tmp = _categoryNames.value
                                tmp?.add(category.categoryName)
                                _categoryNames.value = tmp!!
                            }
                        }
                    }
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }

    }


    fun getMyProfileImageUrl(){
        GlobalScope.async {
            val url = repository.getMyProfileImageUrl(MyProfileImageRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!)).url
            viewModelScope.launch{
                _myProfileImageUrl.value = url
            }
        }
    }


    fun getNoticeFun() { // Spring
        val version = BuildConfig.VERSION_NAME
        CoroutineScope(Dispatchers.Main).launch {
            val result = repositoryNotice.noticeApiCall(NoticeRequest("android", version))
            Log.d("test123","${result.body()}")
            when (result.code()) {
                200 -> { // 성공 코드
                    _noticeResponse.value = result.body()!!.noticeBody
                }
                else -> { // 그 외 나머지 서버 에러 코드
                    _noticeResponse.value = "Error Exist"
                }
            }
        }
    }



//    fun getNotice(){
//        GlobalScope.async {
//            val version = BuildConfig.VERSION_NAME
//            val response = repository.getNotice(NoticeRequest("android",version))
//            viewModelScope.launch{
//                _noticeResponse.value = response
//                noticeAlreadyShow=true
//            }
//        }
//    }


    suspend fun uploadPhoto(postId:Int){
        GlobalScope.async {
            val list = repository.getUploadUrls(GetUploadUrlsInfo(getMimeTypeList(),App.prefs.getValue(tokenKey)!!,postId) )
            for(i in 0 until list.size){
                val file = RequestBody.create(MediaType.parse(_selectedPhotoUriList.value!![i].mimeType),
                    FileUtil.getDownsizedImageBytes(_selectedPhotoUriList.value!![i].filePath))
                repository.uploadImage(file,list[i])
            }
            viewModelScope.launch {
                _isAddPostSuccess.value = Event(true)
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun deletePhotoUriPosition(position: Int){
        val tmpList = _selectedPhotoUriList.value!!
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
        _photoPickerClickSwitch.value = Event(true)
    }


    fun clickNestedCommentReply(groupNum: Int, commentBody: String) {
        _selectedParentCommentBody.value = commentBody
        _selectedCommentGroupNum.value = groupNum
    }


    fun callLoginOut() {
        viewModelScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val logoutResponse = repository.logout(LogoutRequest(App.prefs.getValue("token")!!))
            if(logoutResponse.isSuccess) {
                val key = BuildConfig.TOKEN_KEY
                App.prefs.removeValue(key)
                _startLoginActivity.value = Event(true)
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clearNestedCommentInfo() {
        _selectedParentCommentBody.postValue( "" )
        _selectedCommentGroupNum.postValue( -1 )
    }


    fun clearAddPost() {
        _selectedPhotoUriList.value = arrayListOf()
        _postBody.value = ""
        _selectedCategoryNameInAddPost.value = _categoryNames.value!![0]
        _photoPickerClickSwitch.value = Event(null)
    }


    fun commentBodyClear() {
        _commentBody.value = ""
    }

    override fun scrollTouchBottomCommunityPost() {
        if (_posts.value!!.size > 0) {
            pagingIndex += 1
            endPostId = _posts.value!![_posts.value!!.size - 1].postId
            getPosts()
        }
    }

    override fun scrollTouchTopCommunityPost() {
        clearPosts()
        getPosts()
    }

    fun scrollTopShowPost() {
        getPostContentFromPostId()
        getComments()
    }




    fun clickAddPost() {
        _postCategoryInt.value = 0 // Choose 한 category가 0 (항상 "Select") 이게 함.
    }


    fun clickPostLike() {
        val plusValue =
            if (_isLikePost.value!!) -1
            else 1

        if (_selectedPostPosition.value!! == -1 ) {
            _postFromId.value!!.likeCount = _postFromId.value!!.likeCount + plusValue
            _postFromId.value!!.isLiked = !( _postFromId.value!!.isLiked )
        } else {
            var tmpPosts = _posts.value
            tmpPosts!![_selectedPostPosition.value!!].likeCount =
                _posts.value!![_selectedPostPosition.value!!].likeCount + plusValue
            tmpPosts!![_selectedPostPosition.value!!].isLiked =
                !_posts.value!![_selectedPostPosition.value!!].isLiked
            _posts.postValue(tmpPosts!!)
        }

        _isLikePost.value = !(_isLikePost.value)!!//like button 체크 혹은 해제
        _postInnerValueChangeSwitch.value = !_postInnerValueChangeSwitch.value!!
        addLikePost(plusValue)
    }


    fun clickPostLikeInCommunityContents(position: Int) {
        viewModelScope.async {
            _selectedPostPosition.postValue(position)
            isLikePostInCommunityContents().await()

            val plusValue =
                if (_isLikePost.value!!) -1
                else 1
            var tmpPosts = _posts.value
            tmpPosts!![_selectedPostPosition.value!!].likeCount =
                _posts.value!![_selectedPostPosition.value!!].likeCount + plusValue
            tmpPosts!![_selectedPostPosition.value!!].isLiked =
                !_posts.value!![_selectedPostPosition.value!!].isLiked
            _posts.postValue(tmpPosts!!)
            _isLikePost.value = !(_isLikePost.value)!!//like button 체크 혹은 해제
            _postInnerValueChangeSwitch.value = !_postInnerValueChangeSwitch.value!!
            addLikePost(plusValue)
        }

    }

    fun addLikePost(plusValue: Int) {
        val postIdForAddLikePost : Int
        if (_selectedPostPosition.value!! == -1 ) {
            postIdForAddLikePost = _postId.value!!
        } else {
            postIdForAddLikePost = _posts.value!![_selectedPostPosition.value!!].postId
        }

        GlobalScope.launch {
            val addLikePostInfo = AddLikePostInfo(
                postIdForAddLikePost,
                App.prefs.getValue(tokenKey)!!,
                plusValue
            )
            val resJson = repository.addLikePost(addLikePostInfo)
            viewModelScope.launch {
                if (resJson.get("isSuccess").asBoolean){
                    if (_selectedPostPosition.value!! == -1 ) {
                        _postFromId.value!!.likeCount = resJson.get("like_count").asInt
                    } else {
                        var tmpPosts = _posts.value
                        tmpPosts!![_selectedPostPosition.value!!].likeCount =
                            resJson.get("like_count").asInt
                        _posts.postValue(tmpPosts!!)
                    }

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

        viewModelScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val categoryId = if(userSelectCategories.value!!.size-1>=selectedCategoryPosition.value!!){
                userSelectCategories.value!![selectedCategoryPosition.value!!].categoryId
            }else{
                -1
            }
            val resPosts = repository.getPosts(
                GetPostInfo(
                    pagingIndex,
                    endPostId,
                    categoryId,
                    token!!
                )
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
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))

        }
    }


    fun addLikeComment(plusValue: Int, position: Int) {
        GlobalScope.launch {
            val addLikeCommentInfo = AddLikeCommentInfo(
                _comments.value!![position].commentId,
                App.prefs.getValue(tokenKey)!!,
                plusValue
            )
            val resJson = repository.addLikeComment(addLikeCommentInfo)
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


    override fun clearPosts() {
        _posts.value = ArrayList<PreviewPost>()
        pagingIndex = 0
        endPostId = -1
    }


    fun getComments() {
        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        val getCommentInfo : GetCommentInfo

        if (_selectedPostPosition.value!! == -1 ) {
            getCommentInfo = GetCommentInfo(_postId.value!!, token!!)
        } else {
            getCommentInfo = GetCommentInfo(_posts.value!![_selectedPostPosition.value!!].postId, token!!)
        }

        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val resComments = repository.getComments(getCommentInfo)
            viewModelScope.launch {
                var tmpCommentList = ArrayList<Comment>()
                for(idx in resComments.indices) {
                    if (idx == 0){
                        if(resComments[idx].status == "child" ) { // 그 패턴이면
                            tmpCommentList.add(
                                Comment("", 0, "* Deleted Comment", Timestamp.valueOf("2021-07-13 11:11:11"), 0, "parent", 0, resComments[idx].groupNum, false, false,"" ,-1) //dummyComment
                            )
                            tmpCommentList.add(resComments[idx])
                        }
                        else
                            tmpCommentList.add(resComments[idx])
                    } else {
                        if(resComments[idx].status == "child" && resComments[idx].groupNum != resComments[idx - 1].groupNum ) { // 그 패턴이면
                            tmpCommentList.add(
                                Comment("", 0, "* Deleted Comment", Timestamp.valueOf("2021-07-13 11:11:11"), 0, "parent", 0, resComments[idx].groupNum, false, false,"", -1) // dummyComment
                            )
                            tmpCommentList.add(resComments[idx])
                        }
                        else
                            tmpCommentList.add(resComments[idx])
                    }
                }
                _comments.value = tmpCommentList
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun addComment() {
        if ( _commentBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            lateinit var addCommentInfo: AddCommentInfo
            val postIdForAddComment : Int

            if (_selectedPostPosition.value!! == -1 ) {
                postIdForAddComment = _postId.value!!
            } else {
                postIdForAddComment = _posts.value!![_selectedPostPosition.value!!].postId
            }

            GlobalScope.launch {
                if (_selectedCommentGroupNum.value == -1) { // _selectedCommentGroupNum.value==-1 -> parent인 댓글
                    addCommentInfo = AddCommentInfo(
                        postIdForAddComment,
                        _commentBody.value!!,
                        App.prefs.getValue(tokenKey)!!,
                        "parent",
                        -1
                    )
                } else {
                    addCommentInfo = AddCommentInfo(
                        postIdForAddComment,
                        _commentBody.value!!,
                        App.prefs.getValue(tokenKey)!!,
                        "child",
                        _selectedCommentGroupNum.value!!
                    )
                }
                val isSuccess = repository.addComment(addCommentInfo)

                if (isSuccess) {
                    _commentBody.postValue("")
//                    clearNestedCommentInfo()
                    viewModelScope.launch {
                        if (_selectedPostPosition.value!! == -1 ) {
                            _postFromId.value!!.commentCount = _postFromId.value!!.commentCount + 1
                        } else {
                            var tmpPosts = _posts.value
                            tmpPosts!![_selectedPostPosition.value!!].commentCount =
                                _posts.value!![_selectedPostPosition.value!!].commentCount + 1
                            _posts.postValue(tmpPosts!!)
                        }
                        _commentCountChange.value = Event(true)
                        getComments()
                    }
                    clearNestedCommentInfo()
                }
            }
        }
    }


    fun addPost() {
        if ( _postBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else if(_postCategoryInt.value == 0) {
            _isUnvalidCategorySelect.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                var tmpCategoryId: Int
                if(_postCategoryInt.value!!>=_allCategories.value!!.size){
                    tmpCategoryId = _allClubCategories.value!![_postCategoryInt.value!! - _allCategories.value!!.size].categoryId
                }else{
                    tmpCategoryId = _userSelectCategories.value!![_postCategoryInt.value!!].categoryId
                }
                val key = BuildConfig.TOKEN_KEY
                val addPostInfo = AddPostInfo(
                    "bulletin",
                    "",
                    _postBody.value!!,
                    App.prefs.getValue(key)!!,
                    arrayListOf(),
                    tmpCategoryId
                )
                val res = repository.addPost(addPostInfo)
                val isSuccess = res.isSuccess
                val postId = res.postId
                if (isSuccess) {
                    uploadPhoto(postId)
                } else {
                    ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
                }
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

        if (position == -1) {
            if (_postFromId.value!!.isMine)
                _isPostMine.value = Event(true)
            else
                _isPostMine.value = Event(false)
        } else {
            _selectedPostMorePosition.value = position
            _postId.value = _posts.value!![_selectedPostMorePosition.value!!].postId
            if (_posts.value!![selectedPostMorePosition.value!!].isMine)
                _isPostMine.value = Event(true)
            else
                _isPostMine.value = Event(false)
        }
    }

    fun dismissPostMore() {
        switch(_isPostMorePreventDouble)
    }

    fun clickCommentMore(position: Int) {
        _isCommentMore.value = Event(true)
        _selectedCommentMorePosition.value = position

        if (_comments.value!![_selectedCommentMorePosition.value!!].isMine)
            _isCommentMine.value = Event(true)
        else
            _isCommentMine.value = Event(false)
    }

    fun dismissCommentMore() {
        switch(_isCommentMorePreventDouble)
    }


    fun clickPostReport() {
        _isPostReport.value = Event(true)
        _reportPostBody.value = ""
    }

    fun setIsPostEdit(boolean: Boolean?){
        _isPostEdit.value = boolean
    }

    fun clickPostEdit() {
        if (_selectedPostMorePosition.value == null ) {
            _postBody.value = _postFromId.value!!.postBody
            _postCategoryInt.value = findCategoryIndexById(_postFromId.value!!.categoryId )
        } else {
            _postBody.value = _posts.value!![selectedPostMorePosition.value!!].postBody
            _postCategoryInt.value = findCategoryIndexById(_posts.value!![selectedPostMorePosition.value!!].categoryId )
        }

    }

    fun clickBlockUserPost() { //post, user block
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            var result = repository.blockUser(BlockUserRequest(App.prefs.getValue(tokenKey)!!,_posts.value!![selectedPostMorePosition.value!!].userInfoId))
            _isBlockUser.postValue(Event(result))
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clickBlockUserComment() { // comment, user block
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            var result = repository.blockUser(BlockUserRequest(App.prefs.getValue(tokenKey)!!,_comments.value!![selectedCommentMorePosition.value!!].user_info_id))
            _isBlockUserInComment.postValue(Event(result))
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }

    fun clickPostDelete() {
        val postId : Int
        if (_selectedPostPosition.value!! == -1 ) {
            postId = _postId.value!!
        } else {
            postId = _posts.value!![_selectedPostMorePosition.value!!].postId
        }

        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            var result = repository.deletePostRepository(DeletePostInfo( postId ))
            _isPostDelete.postValue(Event(result))
            _isPostDeleteShowPost.postValue(Event(result))
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }

    }


    fun clickCommentReport() {
        _isCommentReport.value = Event(true)
        _reportCommentBody.value = ""
    }

//    fun clickContactUs() {
//        _isContactUs.value = Event(true)
//        _userRequestBody.value = ""
//    }

    fun clickCommentEdit() {
        _isCommentEdit.value = Event(true)
        _commentEditBody.value = _comments.value!![selectedCommentMorePosition.value!!].commentBody
        _commentBody.value = ""
    }

    fun clickClubJoinRequest() {
        _isClubJoinRequest.value = Event(true)
        _clubJoinRequestBody.value = ""
    }

    fun clickCommentDelete() {
        val commentInt = _comments.value!![_selectedCommentMorePosition.value!!].commentId
        val postId : Int

        if (_selectedPostPosition.value!! == -1 ) {
            postId = _postId.value!!
        } else {
            postId = _posts.value!![_selectedPostPosition.value!!].postId
        }

        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

            var result = repository.deleteCommentRepository(DeleteCommentInfo(commentInt, postId))
            _isCommentDelete.postValue(Event(result))
            _isDeleteCommentSuccess.postValue(Event(result))

            if (result) {
                if (_selectedPostPosition.value!! == -1 ) {
                    _postFromId.value!!.commentCount = _postFromId.value!!.commentCount - 1
                } else {
                    var tmpPosts = _posts.value
                    tmpPosts!![_selectedPostPosition.value!!].commentCount =
                        _posts.value!![_selectedPostPosition.value!!].commentCount - 1
                    _posts.postValue(tmpPosts!!)
                }

                _commentCountChange.postValue(Event(result))
                getComments()
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun getSelectedCategories() { // selected 된 것 불러오기
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)

        GlobalScope.launch {
            var categoryList = repository.getSelectedCategoriesRepository( Token(App.prefs.getValue(tokenKey)!!) )
            viewModelScope.launch {
                categoryList.add(0, Category(-1, "All","t","common","All"))
                _userSelectCategories.value = categoryList
            }

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    suspend fun getCategories():Deferred<Unit> { // 전체 카테고리 불러오기
        ProgressBarUtil._progressBarDialogFlag.value = Event(true)

        return GlobalScope.async {
            var categoryList = repository.getCategories(GetCategoriesRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY),null))
            viewModelScope.launch {
                _allCategories.value?.addAll(categoryList)
                _categoryNames.value = splitCategoryNames(categoryList)

                for ( i in 0 until _allCategories.value!!.size ) {
                    if (_allCategories.value!![i].categoryType == "department") {
                        _departmentACategoryList.value!!.add(_allCategories.value!![i])
                        _departmentBCategoryList.value!!.add(_allCategories.value!![i])
                    } else if (_allCategories.value!![i].categoryType == "common") {
                        _commonCategoryList.value!!.add(_allCategories.value!![i])
                    }
                }

                if (_departmentACategoryList.value!!.map{it.categoryName}[0] != "Choose Your Department A") {
                    departmentACategoryList.value!!.add(0, Category(categoryName = "Choose University Department A", isMember = null, categoryId = -1, categoryType = "dummy_select",categoryAbbreviation = "Choose university Department A") )
                }

                if (_departmentBCategoryList.value!!.map{it.categoryName}[0] != "Choose Your Department B") {
                    _departmentBCategoryList.value!!.add(0, Category(categoryName = "Choose University Department B", isMember = null, categoryId = -1, categoryType = "dummy_select",categoryAbbreviation = "Choose university Department B") )
                }

            }

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            return@async
        }
    }


    fun splitCategoryNames(categoryList: ArrayList<Category>,removeZeroIndex:Boolean=true): ArrayList<String> {
        var categoryNames = ArrayList<String>()
        for (category in categoryList) {
            categoryNames.add(category.categoryName)
        }

        for (category in categoryList) {
            _categoryIdAllList.value!!.add(category.categoryId)
            _categoryNamesForSelection.value!!.add(category.categoryName)
        }

        return categoryNames
    }

    fun isLikePost() { // 내가 좋아요를 눌렀는지 서버에 확인하는 함수
        val postIdForisLikePost : Int
        if (_selectedPostPosition.value!! == -1 ) {
            postIdForisLikePost = _postId.value!!
        } else {
            postIdForisLikePost = _posts.value!![_selectedPostPosition.value!!].postId
        }

        GlobalScope.launch {
            val isLikePostInfo = IsLikePostInfo(
                postIdForisLikePost,
                App.prefs.getValue(tokenKey)!!
            )
            val res = repository.isLikePost(isLikePostInfo)
            viewModelScope.launch {
                _isLikePost.value = res
            }
        }
    }


    suspend fun isLikePostInCommunityContents() : Deferred<Unit> { // 내가 좋아요를 눌렀는지 서버에 확인하는 함수
        return GlobalScope.async {
            val isLikePostInfo = IsLikePostInfo(
                _posts.value!![_selectedPostPosition.value!!].postId,
                App.prefs.getValue(tokenKey)!!
            )
            val res = repository.isLikePost(isLikePostInfo)
            viewModelScope.launch {
                _isLikePost.value = res
            }
            return@async
        }
    }


    fun addPostViewCount() {
        GlobalScope.launch {
            repository.addPostViewCount(AddPostViewCountInfo(_posts.value!![_selectedPostPosition.value!!].postId))
        }
    }

    fun onSelectItemAddPost(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        _selectedCategoryNameInAddPost.value = _categoryNames.value!![pos]
        _postCategoryInt.value = pos - 1
        (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#9329D1"))
        (parent.getChildAt(0) as TextView).textSize = 15.0.toFloat()

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


    override fun editPost() {
        if ( _postBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                val key = BuildConfig.TOKEN_KEY
                val editPostInfo =
                    EditPostInfo(_postBody.value!!, _postId.value!!, App.prefs.getValue(key)!!)
                val result = repository.editPostRepository(editPostInfo)

                viewModelScope.launch {
                    _isEditPostSuccess.value = Event(result)
                    scrollTouchTopCommunityPost()
                }

                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }


    fun editComment() {
        if ( _commentEditBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val commentInt = _comments.value!![_selectedCommentMorePosition.value!!].commentId
                val editCommentInfo = EditCommentInfo(
                    _commentEditBody.value!!,
                    commentInt,
                    App.prefs.getValue(tokenKey)!!
                )
                val result = repository.editCommentRepository(editCommentInfo)
                _isEditCommentSuccess.postValue(Event(result))

                if (result) {
                    clearComments()
                    getComments()
                    _commentBody.postValue("")
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))

            }
        }
    }

    fun reportComment() {
        if ( _reportCommentBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val reportInfo = ReportInfo(
                    App.prefs.getValue(tokenKey)!!,
                    _comments.value!![_selectedCommentMorePosition.value!!].commentId,
                    _reportCommentBody.value!!,
                    "comment"
                )
                val result = repository.reportRepository(reportInfo)
                _isReportCommentSuccess.postValue(Event(result))
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }


    fun reportPost() {
        if ( _reportPostBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                val reportInfo = ReportInfo( App.prefs.getValue(tokenKey)!!, _postId.value!!, _reportPostBody.value!! ,"post")
                val result = repository.reportRepository(reportInfo)
                _isReportPostSuccess.postValue(Event(result))

                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }

//    fun addUserRequest() {
//        if ( _userRequestBody.value!!.isBlank() ) {
//            _isStringBlank.value = Event(true)
//        } else {
//            GlobalScope.launch {
//                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
//
//                val addUserRequestRequest =
//                    AddUserRequestRequest(App.prefs.getValue(tokenKey)!!, _userRequestBody.value!!)
//                val result = repository.addUserRequest(addUserRequestRequest)
//                _isContactUsSuccess.postValue(Event(result))
//
//                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
//            }
//        }
//    }



    fun clearValue( item :  MutableLiveData<String> ) {
        item.value  = ""
    }


    fun clickDialogCancel() {
        _isCancelClick.value = Event(true)
        clearValue(_userRequestBody)
        clearValue(_clubJoinRequestBody)
    }

    fun clickCommentEditDialogCancel() {
        _isCommentEditDialogCancel.value = Event(true)
        clearValue(_commentBody)
    }

    fun clickPostReportDialogCancel() {
        _isReportDialogCancel.value = Event(true)
        clearValue(_reportPostBody)
        clearValue(_reportCommentBody)
    }

    fun clickCommentReportDialogCancel() {
        _isCommentReportDialogCancel.value = Event(true)
        clearValue(_reportPostBody)
        clearValue(_reportCommentBody)
    }


    fun imageSizeCount(position: Int) {
        _imageCount.value = _posts.value!![position].imageUrls.size
    }


    fun categoryIdSelect(id : Int, checked: Boolean){
        if (checked) {
            _categoryIdSelectList.value!!.add(id)
        } else {
            _categoryIdSelectList.value!!.remove(id)
        }
    }


    fun clickApplyCategorySelect(){ // Apply 버튼 누를시 실행
        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            if (_departmentCategoryAInt.value!! != 0) {
                _categoryIdSelectList.value!!.add(_departmentCategoryAInt.value!!)
            }
            if (_departmentCategoryBInt.value!! != 0) {
                _categoryIdSelectList.value!!.add(_departmentCategoryBInt.value!!)
            }

            val tmpIdList = _categoryIdSelectList.value!!.sorted()
            val sortCategoryIdArrayList = ArrayList<Int>()
            sortCategoryIdArrayList.addAll(tmpIdList)

            val result = repository.categorySelectMyPageRepository(CategorySelectMyPageInfo( App.prefs.getValue(tokenKey)!!, sortCategoryIdArrayList!!) )
            _categorySelectApply.postValue(Event(result))

            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clearPostFromId() {
        _postFromId.value = null
    }


    fun getPostContentFromPostId() { // scroll top 때 사용, mainViewModel
        val reFreshPostId : Int

        if (_selectedPostPosition.value!! == -1 ) {
            reFreshPostId = _postId.value!!
        } else {
            reFreshPostId = _posts.value!![_selectedPostPosition.value!!].postId
        }

        val postRequest = PostFromIdRequest(
            reFreshPostId,
            App.prefs.getValue(tokenKey)!!
        )

        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val result = repository.postFromIdRepository(postRequest)
            val errorMessage = result.error
            val postContent = result.post
            when {
                errorMessage == ResponseEnum.NOTEXIST -> { // 에러가 났을 때,
                    _toastMessage.postValue("No results found")
                }
                errorMessage == ResponseEnum.DATABASE -> {
                    _toastMessage.postValue("No results found")
                }
                errorMessage == ResponseEnum.DELETE -> {
                    _toastMessage.postValue("Post is deleted.")
                }
                errorMessage == ResponseEnum.UNKNOWN -> {
                    _toastMessage.postValue("No results found")
                }
                errorMessage == ResponseEnum.DUPLICATE -> {
                    _toastMessage.postValue("Post is duplicated")
                }
                else -> { // 성공했을때
                    _toastMessage.postValue("Success")
                    if (_selectedPostPosition.value!! == -1 ) {
                        _postFromId.postValue ( postContent!! )
                    } else {
                        viewModelScope.launch {
                            _posts.value!![_selectedPostPosition.value!!] = postContent!!
                        }
                    }
                    _isShowPostRefreshSuccess.postValue(Event(true))
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))

        }
    }

    fun onSelectItemDepartmentASelect(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item

        if (pos != 0) {
            _departmentCategoryAInt.value = (parent.selectedItem as Category).categoryId
        }

        (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#9329D1"))
        (parent.getChildAt(0) as TextView).textSize = 14.0.toFloat()
    }


    fun onSelectItemDepartmentBSelect(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if (pos != 0) {
            _departmentCategoryBInt.value = (parent.selectedItem as Category).categoryId
        }

        (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#9329D1"))
        (parent.getChildAt(0) as TextView).textSize = 14.0.toFloat()
    }

    fun clearDepartmentCategory() {
        //_categoryIdSelectList.value = ArrayList<Int>()

        _departmentCategoryAInt.value?.let{
            _categoryIdSelectList.value!!.remove(_departmentCategoryAInt.value!!)
        }

        _departmentCategoryBInt.value?.let{
            _categoryIdSelectList.value!!.remove(_departmentCategoryBInt.value!!)
        }

        _departmentCategoryAInt.value = 0
        _departmentCategoryBInt.value = 0
    }

}
