package com.rudder.util

import androidx.recyclerview.widget.DiffUtil
import com.rudder.data.Comment

class CommentsDiffCallback(private val oldList:ArrayList<Comment>, private val newList: ArrayList<Comment>, private val flag : Boolean) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (flag)
                oldList[oldItemPosition].commentId == newList[newItemPosition].commentId
            else
                false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (flag)
            oldList[oldItemPosition].likeCount == newList[newItemPosition].likeCount
        else
            false
    }
}