package com.rudder.viewModel

import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.local.App
import com.rudder.data.remote.MyPostsRequest
import com.rudder.data.remote.PostsWithMyCommentRequest
import com.rudder.data.repository.Repository
import kotlinx.coroutines.launch

class MyCommentViewModel : MyPostViewModel() {

    fun getPostsWithMyComment(isMore: Boolean) {
        if(!(_noMorePostFlag.value?:false)){ //더 불러올 데이터가 남아있으면 함수 실
            viewModelScope.launch {
                val response = Repository().getPostsWithMyComment(PostsWithMyCommentRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!,_offset.value?:0))
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
            }
        }

    }

    override fun refreshPosts() {
        clearPosts()
        getPostsWithMyComment(false)
    }

    override fun scrollTouchTopCommunityPost() {
        refreshPosts()
    }

    override fun scrollTouchBottomCommunityPost() {
        _offset.value?.let {
            _offset.value = it +1
            getPostsWithMyComment(true)
        }
    }



}