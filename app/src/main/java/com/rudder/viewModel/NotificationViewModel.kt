package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.EditPostInfo
import com.rudder.data.dto.NotificationItem
import com.rudder.data.local.App
import com.rudder.data.remote.GetNotificationsRequest
import com.rudder.data.remote.PostFromIdRequest
import com.rudder.data.remote.ResponseEnum
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationViewModel: MainViewModel()  {


    private val _notificationList = MutableLiveData<ArrayList<NotificationItem>>()
    val notificationList : LiveData<ArrayList<NotificationItem>> = _notificationList
    private val tokenKey = BuildConfig.TOKEN_KEY



    fun getNotifications(){
        viewModelScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

            val getNotificationsResponse =
                Repository().getNotifications(GetNotificationsRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!))
            if (getNotificationsResponse.isSuccess) {
                _notificationList.value = getNotificationsResponse.notifications
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    override fun editPost(){
        if ( _postBody.value!!.isBlank() ) {
            _isStringBlank.value = Event(true)
        } else {
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
                val key = BuildConfig.TOKEN_KEY
                val editPostInfo = EditPostInfo(_postBody.value!!, _postId.value!!, App.prefs.getValue(key)!!)
                val result = Repository().editPostRepository(editPostInfo)

                viewModelScope.launch {
                    _isEditPostSuccess.value = Event(result)
                    getPostContentFromPostIdNotification(_postId.value!!, true)
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }


    fun getPostContentFromPostIdNotification(notificationPostId : Int, isNotificationEdit : Boolean = false) { // notification -> edit post ???, getComment??? ??? ???.
        val postRequest = PostFromIdRequest(
            notificationPostId,
            App.prefs.getValue(tokenKey)!!
        )
        _postId.value = notificationPostId

        GlobalScope.launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val result = Repository().postFromIdRepository(postRequest)
            val errorMessage = result.error
            val postContent = result.post
            when {
                errorMessage == ResponseEnum.NOTEXIST -> { // ????????? ?????? ???,
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
                else -> { // ???????????????
                    _toastMessage.postValue("Success")

                viewModelScope.launch {
                    _postFromId.value = postContent!!
                    setSelectedPostPosition(-1) // selectedPosition -> -1
                    if (!isNotificationEdit) {
                        _isPostFromId.postValue(Event(true))
                        }
                    }
                }

            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }




}