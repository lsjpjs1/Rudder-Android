package com.rudder.util

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.rudder.data.PreviewPost

class PostsDiffCallback() : DiffUtil.Callback() {
    lateinit var oldList:ArrayList<PreviewPost>
    lateinit var newList: ArrayList<PreviewPost>

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].postId == newList[newItemPosition].postId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val res = (oldList[oldItemPosition].commentCount == newList[newItemPosition].commentCount
                && oldList[oldItemPosition].likeCount == newList[newItemPosition].likeCount)
        Log.d("diffcallback", "$oldItemPosition $newItemPosition "+oldList[oldItemPosition].commentCount.toString()+newList[newItemPosition].commentCount.toString())
        return res
    }
}