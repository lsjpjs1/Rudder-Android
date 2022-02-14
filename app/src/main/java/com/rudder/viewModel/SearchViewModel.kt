package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.EditPostInfo
import com.rudder.data.GetPostInfo
import com.rudder.data.local.App
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchViewModel : MainViewModel() {
    private val _searchWord = MutableLiveData<String>()
    val searchWord: LiveData<String> = _searchWord
    private val _selectedSearchPostPosition = MutableLiveData<Int>()

    private val _isScrollTouch = MutableLiveData<Event<Boolean>>()



    val selectedSearchPostPosition: LiveData<Int> = _selectedSearchPostPosition
    val isScrollTouch: LiveData<Event<Boolean>> = _isScrollTouch



    init {
        postMode = PostMode.SEARCH
        clearSearchPost()
    }

    override fun editPost(){
        if ( _postBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                val key = BuildConfig.TOKEN_KEY
                val editPostInfo =
                    EditPostInfo(_postBody.value!!, _postId.value!!, App.prefs.getValue(key)!!)
                val result = Repository().editPostRepository(editPostInfo)

                viewModelScope.launch {
                    _isEditPostSuccess.value = Event(result)
                    clearPosts()
                    searchPost(false)
                }

                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }

    override fun scrollTouchBottomCommunityPost() {
        if (_posts.value!!.size > 0) {
            pagingIndex += 1
            endPostId = _posts.value!![_posts.value!!.size - 1].postId
            searchPost(true)
        }
    }

    override fun scrollTouchTopCommunityPost() {
        clearPosts()
        searchPost(false)
    }

    fun searchPost(isScroll: Boolean){
        _isScrollTouch.value = Event(true)

        val key = BuildConfig.TOKEN_KEY
        val token = App.prefs.getValue(key)
        Log.d("search_hello", "searchPost")
        GlobalScope.launch {
            val resPosts = if (isScroll){
                Repository().getPosts(
                    GetPostInfo(
                        pagingIndex,
                        endPostId,
                        -1,
                        token!!,
                        _searchWord.value!!
                    )
                )
            }else{
                Repository().getPosts(
                    GetPostInfo(
                        -1,
                        -1,
                        -1,
                        token!!,
                        _searchWord.value!!
                    )
                )
            }

            viewModelScope.launch {
                if (isScroll){
                    val oldPosts = _posts.value
                    oldPosts!!.addAll(resPosts)
                    Log.d("oldPost", oldPosts.toString())
                    _posts.value = oldPosts!!
                }else{
                    _posts.value = resPosts
                }
                _isScrollTouch.value = Event(false)

            }
        }
    }



    fun clearSearchPost(){
        Log.d("claersearch_hello","clearsearch")
        _posts.value = arrayListOf()
        //_searchWord.value = MutableLiveData<String>().value
    }


    fun setSearchWord(string: String){
        _searchWord.value = string
    }

//    fun setPostTmp(position: Int) {
//        _selectedSearchPostPosition.value = position
//        Log.d("setselectpos",_selectedSearchPostPosition.value!!.toString())
//    }

//    fun setSelectedPostPosition(position: Int) {
//        _selectedPostPosition.value = position
//        Log.d("setselectpos",_selectedPostPosition.value!!.toString())
//    }


}