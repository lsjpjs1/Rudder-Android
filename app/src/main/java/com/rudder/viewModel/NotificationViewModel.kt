package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.data.PreviewPost
import com.rudder.data.dto.NotificationItem
import java.sql.Timestamp

class NotificationViewModel: MainViewModel()  {


    private val _notificationList = MutableLiveData<ArrayList<NotificationItem>>()

    val notificationList : LiveData<ArrayList<NotificationItem>> = _notificationList



    init {
        _notificationList.value = arrayListOf(
            NotificationItem(
                "hello_1",
                0,
                0,
                0,
                1215,
                0
            ),
            NotificationItem(
                "hello_2",
                1,
                1,
                11,
                1,
                39
            ),
            NotificationItem(
                "hello_3",
                0,
                2,
                22,
                1303,
                2
            ),
            NotificationItem(
                "hello_4",
                1,
                3,
                33,
                34,
                40
            )

        )
    }


}