package com.rudder.viewModel

import androidx.lifecycle.ViewModel
import com.rudder.data.dto.PostMessage
import com.rudder.databinding.PostMessageItemBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat

class PostMessageViewModel: ViewModel() {
    /// 21 01 15 real final

    fun getPostMessages() : ArrayList<PostMessage> {
        return arrayListOf<PostMessage>(
            PostMessage(
                receiveUserInfoId = 0,
                sendUserInfoId = 0,
                messageBody = "",
                isRead = false,
                messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
                postMessageId = 0,
                sendUserNickname = "tjdgns",
                receiveUserNickname = "tmp1"
            ),
            PostMessage(
                receiveUserInfoId = 0,
                sendUserInfoId = 0,
                messageBody = "",
                isRead = false,
                messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
                postMessageId = 0,
                sendUserNickname = "xorud",
                receiveUserNickname = "tmp2"
            ),
            PostMessage(
                receiveUserInfoId = 0,
                sendUserInfoId = 0,
                messageBody = "",
                isRead = false,
                messageSendTime = Timestamp.valueOf("2021-07-13 11:11:11"),
                postMessageId = 0,
                sendUserNickname = "alsgh",
                receiveUserNickname = "tmp3"
            )
        )
    }

}