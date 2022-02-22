package com.rudder.ui.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.NotificationItem
import com.rudder.data.dto.NotificationType
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.NotificationContentsItemBinding
import com.rudder.util.NotificationAdapterCallback

class NotificationAdapter(val notificationAdapterCallback: NotificationAdapterCallback) : BaseAdapter<NotificationItem,NotificationContentsItemBinding>(diffUtil, R.layout.notification_contents_item) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return (oldItem.notificationId == newItem.notificationId && oldItem.itemTime == newItem.itemTime)
            }

            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return oldItem.notificationId == newItem.notificationId
            }
        }



    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        val notificationType = getItem(position).notificationType

        holder.viewBinding.notificationType.text = notificationType.alertMessage
        holder.viewBinding.notificationBody.text = getItem(position).itemBody
        holder.viewBinding.notificationTime.text = getItem(position).itemTime.toString()


        holder.viewBinding.notificationTopLevelCL.setOnClickListener {

            if (notificationType == NotificationType.COMMENT) { //post에 대한 알림이면
                notificationAdapterCallback.onClickPostNotification(getItem(position).itemId)
            } else { // post message room에 대한 알림이면
                notificationAdapterCallback.onClickPostMessageRoomNotification(getItem(position).itemId)
            }

        }
    }





}

