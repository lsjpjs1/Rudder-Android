package com.rudder.ui.adapter

import android.util.Log
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import com.rudder.R
import com.rudder.data.dto.PostMessage
import com.rudder.data.dto.PostMessageRoom
import com.rudder.data.dto.ProfileImage
import com.rudder.databinding.PostMessageItemBinding

class PostMessageAdapter : BaseAdapter<PostMessageRoom, PostMessageItemBinding>(diffUtil, R.layout.post_message_item)  {


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PostMessageRoom>() {
            override fun areContentsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return oldItem.postMessageRoomId == newItem.postMessageRoomId
            }

            override fun areItemsTheSame(oldItem: PostMessageRoom, newItem: PostMessageRoom): Boolean {
                return oldItem.postMessageRoomId == newItem.postMessageRoomId //수정 해야됨
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.viewBinding.postMessageItemSenderNickNameTV.text = getItem(position).userId
        holder.viewBinding.postMessageItemDateTV.text = getItem(position).messageSendTime.toString()

        Log.d("asd","${getItem(position)}")

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val superHolder = super.onCreateViewHolder(parent, viewType)
        val lp = superHolder.viewBinding.root.layoutParams
        lp.height = (3000*0.15).toInt()
        lp.width = (3000*0.2).toInt()
        superHolder.viewBinding.root.layoutParams = lp
        return superHolder
    }




}