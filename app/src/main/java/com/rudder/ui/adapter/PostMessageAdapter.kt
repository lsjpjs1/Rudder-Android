package com.rudder.ui.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.data.dto.ProfileImage
import com.rudder.databinding.PostMessageItemBinding
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.util.LocaleUtil
import com.rudder.util.PostMessageAdapterCallback
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class PostMessageAdapter(
    val postMessageAdapterCallback: PostMessageAdapterCallback,
    val context: Context

    ) : BaseAdapter<PostMessageRoom, PostMessageItemBinding>(diffUtil, R.layout.post_message_item)  {

    val MAX_POST_MESSAGE_BODY_LENGTH = 80


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostMessageRoom>() {
            override fun areContentsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return (oldItem.postMessageRoomId == newItem.postMessageRoomId && oldItem.messageSendTime == newItem.messageSendTime)
            }

            override fun areItemsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return oldItem.postMessageRoomId == newItem.postMessageRoomId //수정 해야됨
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        val timeago = PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(getItem(position).messageSendTime.time))

        holder.viewBinding.postMessageItemSenderNickNameTV.text = getItem(position).userId
        holder.viewBinding.postMessageItemDateTV.text = timeago

        val postMessageBody = getItem(position).postMessageBody

        if (postMessageBody.length > 50 ) {
            val subBody = postMessageBody.substring(0,MAX_POST_MESSAGE_BODY_LENGTH) + "  ..."
            holder.viewBinding.postMessageItemMessageBodyTV.text = subBody

        } else {
            holder.viewBinding.postMessageItemMessageBodyTV.text = postMessageBody

        }

        //holder.viewBinding.postMessageItemMessageBodyTV.text = getItem(position).postMessageBody

        holder.viewBinding.postMessageItemCL.setOnClickListener {
            postMessageAdapterCallback.onClickPostMessageRoom(getItem(position).postMessageRoomId)
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