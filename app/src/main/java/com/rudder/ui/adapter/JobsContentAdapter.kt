package com.rudder.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.dto.JobsInfo
import com.rudder.databinding.JobsItemBinding
import com.rudder.util.JobsContentOnclickListener
import com.rudder.util.LocaleUtil
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class JobsContentAdapter(jobsContentOnclickListener: JobsContentOnclickListener)
    : ListAdapter<JobsInfo, RecyclerView.ViewHolder>(JobContentDiffCallback()) {


    class JobContentDiffCallback : DiffUtil.ItemCallback<JobsInfo>() {
        override fun areItemsTheSame(oldItem: JobsInfo, newItem: JobsInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }


        override fun areContentsTheSame(oldItem: JobsInfo, newItem: JobsInfo): Boolean {
            return oldItem == newItem
        }
    }


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


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as JobsViewHolder
        val jobInfoItem = getItem(position) as JobsInfo
        holder.bind(jobInfoItem)
    }



    class JobsViewHolder(private val jobsItemBinding : JobsItemBinding, jobsContentOnclickListener: JobsContentOnclickListener)
        : RecyclerView.ViewHolder(jobsItemBinding.root), View.OnClickListener {
        companion object {
            const val MAX_COMPANY_BODY_LENGTH = 30
            const val MAX_JOB_TITLE_BODY_LENGTH = 70
        }

        private var jobsContentOnclickListener : JobsContentOnclickListener? = null

        init {
            jobsItemBinding.jobsMainCL.setOnClickListener(this)
            jobsItemBinding.jobsItemsHeart.setOnClickListener(this)
            this.jobsContentOnclickListener = jobsContentOnclickListener
        }

        fun bind(jobsItem: JobsInfo) {
            val timeago = PrettyTime(LocaleUtil().getSystemLocale(jobsItemBinding.root.context)).format(Date(jobsItem.postDate.time))

            if (jobsItem.jobType == "") {
                jobsItemBinding.jobsTypeTV.visibility = View.GONE
            }


            if (jobsItem.companyName.length > MAX_COMPANY_BODY_LENGTH ) {
                val subBody = jobsItem.companyName.substring(0, MAX_COMPANY_BODY_LENGTH) + "  ..."
                jobsItemBinding.jobsCompanyTV.text = subBody
            } else {
                jobsItemBinding.jobsCompanyTV.text = jobsItem.companyName
            }


            if (jobsItem.jobTitle.length > MAX_JOB_TITLE_BODY_LENGTH ) {
                val subBody = jobsItem.jobTitle.substring(0, MAX_JOB_TITLE_BODY_LENGTH) + "  ..."
                jobsItemBinding.jobsTitleTV.text = subBody
            } else {
                jobsItemBinding.jobsTitleTV.text = jobsItem.jobTitle
            }

            jobsItemBinding.jobsSalaryTV.text = jobsItem.salary
            jobsItemBinding.jobsTypeTV.text = jobsItem.jobType
            jobsItemBinding.jobsPostTimeTV.text = timeago

            jobsItemBinding.jobsMainCL.tag = jobsItem.jobPostId

            jobsItem.companyImage?.let{
                Glide.with(jobsItemBinding.root.context)
                    .load(jobsItem.companyImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(jobsItemBinding.jobsItemCompanyIcon)
            }

            //jobsItemBinding.jobsItemCompanyIcon.background = ContextCompat.getDrawable(jobsItemBinding.root.context, R.drawable.edge)

            if (jobsItem.isSaved) { // heart를 누른, saved 된 Item 이라면
                //jobsItemBinding.jobsItemsHeart.tag = "not border"

                jobsItemBinding.jobsItemsHeart.setTag(R.id.borderTag, "not border")
                jobsItemBinding.jobsItemsHeart.setTag(R.id.jobIdTag, jobsItem.jobPostId)
                jobsItemBinding.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                //jobsItemBinding.jobsItemsHeart.tag = "border"

                jobsItemBinding.jobsItemsHeart.setTag(R.id.borderTag, "not border")
                jobsItemBinding.jobsItemsHeart.setTag(R.id.jobIdTag, jobsItem.jobPostId)
                jobsItemBinding.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }

        override fun onClick(view: View?) {
            if (view is ImageView) { // heart 클릭시
                this.jobsContentOnclickListener?.onClickImageView(view = view, position = bindingAdapterPosition)
            } else {
                this.jobsContentOnclickListener?.onClickContainerView(view = view!!, position = bindingAdapterPosition, viewTag = view.tag.toString())
            }
        }
    }



}