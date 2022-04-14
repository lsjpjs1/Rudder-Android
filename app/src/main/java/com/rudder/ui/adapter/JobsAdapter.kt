package com.rudder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rudder.data.dto.JobsInfo
import com.rudder.databinding.JobsItemBinding
import com.rudder.util.CustomOnclickListener

class JobsAdapter(private val jobsItemList : ArrayList<JobsInfo>, customOnclickListener: CustomOnclickListener)
    : RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

    private var customOnclickListener : CustomOnclickListener? = null

    init {
        this.customOnclickListener = customOnclickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder( JobsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
            ), this.customOnclickListener!!
        )
    }

    override fun onBindViewHolder(jobsViewHolder: JobsViewHolder, position: Int) {
        jobsViewHolder.bind(jobsItemList[jobsViewHolder.bindingAdapterPosition])

    }

    override fun getItemCount(): Int = jobsItemList.size






    class JobsViewHolder(private val jobsItemBinding : JobsItemBinding, customOnclickListener: CustomOnclickListener) : RecyclerView.ViewHolder(jobsItemBinding.root), View.OnClickListener {

        private var customOnclickListener : CustomOnclickListener? = null

        init {
            jobsItemBinding.jobsMainCL.setOnClickListener(this)
            this.customOnclickListener = customOnclickListener
        }

        fun bind(jobsItem: JobsInfo) {
            jobsItemBinding.jobsTitleTV.text = jobsItem.JobsTitle
        }

        override fun onClick(view: View?) {
            this.customOnclickListener?.onClickView(view = view!!, position = bindingAdapterPosition)
        }
    }


}