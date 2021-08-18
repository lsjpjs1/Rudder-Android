package com.rudder.util

import androidx.recyclerview.widget.DiffUtil
import com.rudder.data.remote.Category


class CategoriesDiffCallback(private val oldList:ArrayList<Category>, private val newList: ArrayList<Category>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].categoryId == newList[newItemPosition].categoryId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].categoryName == newList[newItemPosition].categoryName)
    }
}