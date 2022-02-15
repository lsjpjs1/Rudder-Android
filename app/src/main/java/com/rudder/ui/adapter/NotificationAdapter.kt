package com.rudder.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.NotificationItem
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.NotificationContentsItemBinding
import com.rudder.util.NotificationAdapterCallback

class NotificationAdapter(val notificationAdapterCallback: NotificationAdapterCallback) : BaseAdapter<NotificationItem,NotificationContentsItemBinding>(diffUtil, R.layout.notification_contents_item) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return (oldItem.notificationId == newItem.notificationId && oldItem.notificationTime == newItem.notificationTime)
            }

            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return oldItem.notificationId == newItem.notificationId
            }
        }



    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.viewBinding.notificationType.text = getItem(position).notificationType.toString()
        holder.viewBinding.notificationId.text = getItem(position).notificationId.toString()
        holder.viewBinding.notificationBody.text = getItem(position).notificationBody
        holder.viewBinding.notificationTime.text = getItem(position).notificationTime.toString()


        holder.viewBinding.notificationTopLevelCL.setOnClickListener {
            notificationAdapterCallback.onClickPostNotification(1206)
        }
    }





}

