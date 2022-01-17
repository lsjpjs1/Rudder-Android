package com.rudder.viewModel

import androidx.lifecycle.ViewModel
import com.rudder.data.dto.PostMessage
import com.rudder.databinding.PostMessageItemBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat

class PostMessageViewModel: ViewModel() {

    fun getPostMessages() : ArrayList<PostMessage> {
        return arrayListOf<PostMessage>(
            PostMessage(
            receiveUserInfoId = 0,
            sendUserInfoId = 0,
            messageBody = "",
            isRead = false,
            messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
            postMessageId = 0,
            sendUserNickname = "tjdgns"
            ),
            PostMessage(
                receiveUserInfoId = 0,
                sendUserInfoId = 0,
                messageBody = "",
                isRead = false,
                messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
                postMessageId = 0,
                sendUserNickname = "xorud"
            ),
            PostMessage(
                receiveUserInfoId = 0,
                sendUserInfoId = 0,
                messageBody = "",
                isRead = false,
                messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
                postMessageId = 0,
                sendUserNickname = "alsgh"
            )
        )
    }

}