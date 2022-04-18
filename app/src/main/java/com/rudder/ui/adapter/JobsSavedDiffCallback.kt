package com.rudder.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rudder.data.dto.JobsInfo

class JobsSavedDiffCallback : DiffUtil.ItemCallback<JobsInfo>() {
    override fun areItemsTheSame(oldItem: JobsInfo, newItem: JobsInfo): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: JobsInfo, newItem: JobsInfo): Boolean {
        return oldItem == newItem
    }
}