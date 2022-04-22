package com.rudder.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.dto.JobsInfo
import com.rudder.databinding.JobsItemBinding
import com.rudder.util.CustomOnclickListener
import com.rudder.util.JobsContentOnclickListener

class JobsContentAdapter(private val jobsItemList : ArrayList<JobsInfo>, jobsContentOnclickListener: JobsContentOnclickListener)
    : RecyclerView.Adapter<JobsContentAdapter.JobsViewHolder>() {

    private var jobsContentOnclickListener : JobsContentOnclickListener? = null

    init {
        this.jobsContentOnclickListener = jobsContentOnclickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder( JobsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
            ), this.jobsContentOnclickListener!!
        )
    }

    override fun onBindViewHolder(jobsViewHolder: JobsViewHolder, position: Int) {
        jobsViewHolder.bind(jobsItemList[jobsViewHolder.bindingAdapterPosition])

    }

    override fun getItemCount(): Int = jobsItemList.size



    class JobsViewHolder(private val jobsItemBinding : JobsItemBinding, jobsContentOnclickListener: JobsContentOnclickListener)
        : RecyclerView.ViewHolder(jobsItemBinding.root), View.OnClickListener {

        private var jobsContentOnclickListener : JobsContentOnclickListener? = null

        init {
            jobsItemBinding.jobsMainCL.setOnClickListener(this)
            jobsItemBinding.jobsItemsHeart.setOnClickListener(this)
            this.jobsContentOnclickListener = jobsContentOnclickListener
        }

        fun bind(jobsItem: JobsInfo) {
            jobsItemBinding.jobsTitleTV.text = jobsItem.jobTitle

            if (jobsItem.isSaved) { // heart를 누른, saved 된 Item 이라면
                jobsItemBinding.jobsItemsHeart.tag = "not border"
                jobsItemBinding.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                jobsItemBinding.jobsItemsHeart.tag = "border"
                jobsItemBinding.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }

        override fun onClick(view: View?) {
            if (view is ImageView) { // heart 클릭시
                this.jobsContentOnclickListener?.onClickImageView(view = view!!, position = bindingAdapterPosition)
            } else {
                this.jobsContentOnclickListener?.onClickContainerView(view = view!!, position = bindingAdapterPosition)
            }
        }
    }


}