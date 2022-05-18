package com.rudder.viewModel

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


class SearchViewModel : MainViewModel(), ViewModelInterface {
    private val _searchWord = MutableLiveData<String>()
    val searchWord: LiveData<String> = _searchWord
    private val _selectedSearchPostPosition = MutableLiveData<Int>()

    private val _isScrollTouch = MutableLiveData<Event<Boolean>>()
    private val _isSearchWordValid = MutableLiveData<Event<Boolean>>()

    val isScrollTouch: LiveData<Event<Boolean>> = _isScrollTouch
    val isSearchWordValid: LiveData<Event<Boolean>> = _isSearchWordValid
    

    init {
        postMode = PostMode.SEARCH
        clearSearchPost()
        _searchWord.value = ""


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
                    scrollTouchTopCommunityPost()
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
        if ( _searchWord.value!!.isBlank() || _searchWord.value!!.length < 2) {
            _isSearchWordValid.value = Event(true)
        } else {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            _isScrollTouch.value = Event(true)
            val key = BuildConfig.TOKEN_KEY
            val token = App.prefs.getValue(key)
            val searchWordSplit = _searchWord.value!!.trim().split(' ')
            val validSearchWord = searchWordSplit.joinToString(" ")

            GlobalScope.launch {
                val resPosts = if (isScroll) {
                    Repository().getPosts(
                        GetPostInfo(
                            token!!,
                            -1,
                            endPostId,
                            validSearchWord
                        )
                    )
                } else {
                    Repository().getPosts(
                        GetPostInfo(
                            token!!,
                            -1,
                            -1,
                            validSearchWord
                        )
                    )
                }

                viewModelScope.launch {
                    if (isScroll) {
                        val oldPosts = _posts.value
                        oldPosts!!.addAll(resPosts)
                        _posts.value = oldPosts!!
                    } else {
                        _posts.value = resPosts
                    }
                    _isScrollTouch.value = Event(false)
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clearSearchPost(){
        _posts.value = arrayListOf()
    }


    fun setSearchWord(string: String){
        _searchWord.value = string
    }


}
