package com.rudder.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.RoomPostMessageItemBinding

class RoomPostMessagesAdapter(
       val context: Context?
) : BaseAdapter<PostMessage, RoomPostMessageItemBinding>(diffUtil ,R.layout.room_post_message_item) {
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
        holder.viewBinding.roomPostMessageTimeTV.text = getItem(position).messageSendTime.toString()
        if(!getItem(position).isSender){
            holder.viewBinding.roomPostMessageCL.background = ResourcesCompat.getDrawable(context!!.resources,R.color.light_grey_2,null)
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        val superHolder = super.onCreateViewHolder(parent, viewType)
//        val lp = superHolder.viewBinding.root.layoutParams
//        lp.height = (3000*0.15).toInt()
//        superHolder.viewBinding.root.layoutParams = lp
//        return superHolder
//    }
}