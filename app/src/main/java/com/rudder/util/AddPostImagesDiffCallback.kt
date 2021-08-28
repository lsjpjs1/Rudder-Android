package com.rudder.util

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.rudder.data.remote.Category

class AddPostImagesDiffCallback (private val oldList:ArrayList<Uri>, private val newList: ArrayList<Uri>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition]== newList[newItemPosition])
    }
}