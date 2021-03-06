package com.rudder.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.NotificationItem
import com.rudder.data.dto.NotificationType
import com.rudder.data.local.App
import com.rudder.databinding.NotificationContentsItemBinding
import com.rudder.util.LocaleUtil
import com.rudder.util.NotificationAdapterCallback
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NotificationAdapter(val notificationAdapterCallback: NotificationAdapterCallback, val context: Context) : BaseAdapter<NotificationItem,NotificationContentsItemBinding>(diffUtil, R.layout.notification_contents_item) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return (oldItem.notificationId == newItem.notificationId && oldItem.itemTime == newItem.itemTime)
            }

            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
                return oldItem.notificationId == newItem.notificationId
            }
        }
        const val MAX_NOTIFICATION_BODY_LENGTH = 50
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val notificationType = getItem(position).notificationType
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(getItem(position).itemTime.time))
        val notificationBody = getItem(position).itemBody

        if (notificationBody.length > 50 ) {
            val subBody = notificationBody.substring(0,
                MAX_NOTIFICATION_BODY_LENGTH) + "  ..."
            holder.viewBinding.notificationBody.text = subBody
        } else {
            holder.viewBinding.notificationBody.text = notificationBody
        }

        holder.viewBinding.notificationType.text = notificationType.alertMessage
        holder.viewBinding.notificationTime.text = timeago

        holder.viewBinding.notificationTopLevelCL.setOnClickListener {

            val getNotificationValue = App.prefs.getValueInt("notification")
            if (getNotificationValue!! > 0) {
                App.prefs.setValueInt("notification", getNotificationValue!!.minus(1) )
            }


            if (notificationType == NotificationType.COMMENT) { //post??? ?????? ????????????
                notificationAdapterCallback.onClickPostNotification(getItem(position).itemId)
            } else if (notificationType == NotificationType.POST_MESSAGE){ // post message room??? ?????? ????????????
                notificationAdapterCallback.onClickPostMessageRoomNotification(getItem(position).itemId)
            } else if (notificationType == NotificationType.NESTED_COMMENT) {
                notificationAdapterCallback.onClickPostNotification(getItem(position).itemId)
            }
        }
    }





}

