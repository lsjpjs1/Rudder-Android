package com.rudder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rudder.data.dto.JobsInfo
import com.rudder.databinding.JobsItemBinding

class JobsAdapter(private val jobsItemList : ArrayList<JobsInfo>) : RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder( JobsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(jobsViewHolder: JobsViewHolder, position: Int) {
        jobsViewHolder.bind(jobsItemList[jobsViewHolder.bindingAdapterPosition])

    }

    override fun getItemCount(): Int = jobsItemList.size


    class JobsViewHolder(private val jobsItemBinding : JobsItemBinding) : RecyclerView.ViewHolder(jobsItemBinding.root) {

        fun bind(jobsItem: JobsInfo) {
            jobsItemBinding.jobsTitleTV.text = jobsItem.JobsTitle
        }
    }

}