package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.PreviewPost
import com.rudder.data.local.App
import com.rudder.data.remote.MyPostsRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.launch

open class MyPostViewModel : MainViewModel() {


    protected val _offset = MutableLiveData<Int>()
    protected val _noMorePostFlag = MutableLiveData<Boolean>()

    val offset : LiveData<Int> = _offset
    init {
        _offset.value = 0
        _noMorePostFlag.value = false
    }

    fun getMyPosts(isMore: Boolean) {
        if(!(_noMorePostFlag.value?:false)){ //더 불러올 데이터가 남아있으면 함수 실
            viewModelScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val response = Repository().getMyPosts(MyPostsRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!,_offset.value?:0))
                if (response.isSuccess){
                    if (response.posts.size == 0){
                        _noMorePostFlag.value = true
                        return@launch
                    }
                    if (isMore){
                        _posts.value?.let {
                            val tmpPosts = it
                            tmpPosts.addAll(response.posts)
                            _posts.value = tmpPosts
                        }
                    } else {
                        _posts.value = response.posts
                    }

                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))

            }
        }

    }

    open fun refreshPosts() {
        clearPosts()
        getMyPosts(false)
    }

    override fun clearPosts() {
            _posts.value = ArrayList<PreviewPost>()
            _offset.value = 0
            _noMorePostFlag.value = false
    }

    override fun scrollTouchTopCommunityPost() {
        refreshPosts()
    }

    override fun scrollTouchBottomCommunityPost() {
        _offset.value?.let {
            _offset.value = it +1
            getMyPosts(true)
        }
    }
}