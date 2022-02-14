package com.rudder.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.NotificationItem
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.NotificationContentsItemBinding

class NotificationAdapter() : BaseAdapter<NotificationItem,NotificationContentsItemBinding>(diffUtil, R.layout.notification_contents_item) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                //return (oldItem.postMessageRoomId == newItem.postMessageRoomId && oldItem.messageSendTime == newItem.messageSendTime)
            }

            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return oldItem.notificationId == newItem.notificationId
            }
        }



    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.viewBinding.postMessageItemSenderNickNameTV.text = getItem(position).userId
        holder.viewBinding.postMessageItemDateTV.text = getItem(position).messageSendTime.toString()
        holder.viewBinding.postMessageItemMessageBodyTV.text = getItem(position).postMessageBody

        holder.viewBinding.postMessageItemCL.setOnClickListener {
            postMessageAdapterCallback.onClickPostMessageRoom(getItem(position).postMessageRoomId)
        }
    }





}

