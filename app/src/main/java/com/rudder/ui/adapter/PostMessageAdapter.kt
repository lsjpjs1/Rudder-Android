package com.rudder.ui.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.data.dto.ProfileImage
import com.rudder.databinding.PostMessageItemBinding
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.util.LocaleUtil
import com.rudder.util.PostMessageAdapterCallback
import kotlinx.android.synthetic.main.post_message_item.view.*
import kotlinx.android.synthetic.main.post_preview.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class PostMessageAdapter(
    val postMessageAdapterCallback: PostMessageAdapterCallback,
    val context: Context

    ) : BaseAdapter<PostMessageRoom, PostMessageItemBinding>(diffUtil, R.layout.post_message_item)  {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostMessageRoom>() {
            override fun areContentsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return (oldItem.postMessageRoomId == newItem.postMessageRoomId && oldItem.messageSendTime == newItem.messageSendTime)
            }

            override fun areItemsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return oldItem.postMessageRoomId == newItem.postMessageRoomId //수정 해야됨
            }
        }
        const val MAX_POST_MESSAGE_BODY_LENGTH = 80

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

        holder.viewBinding.postMessageItemCL.setOnClickListener {
            postMessageAdapterCallback.onClickPostMessageRoom(getItem(position).postMessageRoomId)
        }

        Glide.with(holder.viewBinding.root.postMessageItemSenderImageView.context)
            .load(getItem(position).userProfileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.viewBinding.root.postMessageItemSenderImageView)

    }

}