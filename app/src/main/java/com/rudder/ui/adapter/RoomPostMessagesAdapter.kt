package com.rudder.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.RoomPostMessageItemBinding
import java.text.SimpleDateFormat
import java.util.*

class RoomPostMessagesAdapter(
       val context: Context?
) : BaseAdapter<PostMessage, RoomPostMessageItemBinding>(diffUtil ,R.layout.room_post_message_item) {

    private val purpleRudder by lazy { ContextCompat.getColor(context!!,R.color.purple_rudder) }
    private val lightPurpleRudder by lazy { ContextCompat.getColor(context!!,R.color.light_purple_rudder) }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostMessage>() {
            override fun areContentsTheSame(oldItem: PostMessage, newItem: PostMessage): Boolean {
                return oldItem.postMessageId == newItem.postMessageId
            }

            override fun areItemsTheSame(oldItem: PostMessage, newItem: PostMessage): Boolean {
                return oldItem.postMessageId == newItem.postMessageId //수정 해야됨
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.viewBinding.roomPostMessageBodyTV.text = getItem(position).postMessageBody

        if(!getItem(position).isSender){
            holder.viewBinding.roomPostMessageTypeTV.text = "Received Mail"
            holder.viewBinding.roomPostMessageTypeTV.setTextColor(lightPurpleRudder)

        } else {
            holder.viewBinding.roomPostMessageTypeTV.text = "Sent Mail"
            holder.viewBinding.roomPostMessageTypeTV.setTextColor(purpleRudder)

        }

        val messageTime = SimpleDateFormat("yy/MM/dd HH:mm").format(getItem(position).messageSendTime)
        holder.viewBinding.roomPostMessageTimeTV.text = messageTime
    }
}