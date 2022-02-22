package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.PreviewPost
import com.rudder.data.dto.NotificationItem
import com.rudder.data.local.App
import com.rudder.data.remote.GetNotificationsRequest
import com.rudder.data.repository.Repository
import kotlinx.coroutines.launch
import java.sql.Timestamp

class NotificationViewModel: MainViewModel()  {


    private val _notificationList = MutableLiveData<ArrayList<NotificationItem>>()

    val notificationList : LiveData<ArrayList<NotificationItem>> = _notificationList



    fun getNotifications(){
        viewModelScope.launch {
            val getNotificationsResponse =
                Repository().getNotifications(GetNotificationsRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!))
            if (getNotificationsResponse.isSuccess) {
                _notificationList.value = getNotificationsResponse.notifications
            }
        }
    }




}