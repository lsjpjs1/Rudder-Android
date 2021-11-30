package com.rudder.viewModel

import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.EditPostInfo
import com.rudder.data.local.App
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SearchViewModel : MainViewModel() {
    override var qwe = false
    init {
        postMode = PostMode.SEARCH
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

    override fun scrollTouchBottom() {
        if (_posts.value!!.size > 0) {
            pagingIndex += 1
            endPostId = _posts.value!![_posts.value!!.size - 1].postId
            searchPost(true)
        }
    }

    override fun scrollTouchTop() {
        clearPosts()
        searchPost(false)
    }
}