package com.rudder.ui.adapter

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.util.CustomOnclickListener
import com.rudder.util.LocaleUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.post_preview.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class MainPostPreviewAdapter(
    listener: CustomOnclickListener,
    context: Context,
    viewModel: MainViewModel,
    lifecycleOwner: LifecycleOwner,
) : PostPreviewAdapter<MainViewModel>(listener,context,viewModel,lifecycleOwner) {

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(getItem(position).postTime.time))
        val imageCount = getItem(position).imageUrls.size
        var mLastClickTime: Long = 0

        holder.postPreviewBinding.post = getItem(position)
        holder.postPreviewBinding.timeago = timeago
        holder.postPreviewBinding.mainVM = viewModel
        holder.postPreviewBinding.position = position
        holder.postPreviewBinding.also {
            it.post = getItem(position)
            it.timeago = timeago
            it.maxpostbodylength = this.MAX_POST_BODY_LENGTH
        }
        holder.postPreviewBinding.postPreview.setOnClickListener {
            listener.onClickView(holder.postPreviewBinding.postPreview, position)
        }

        if (getItem(position).isLiked) {
            holder.postPreviewBinding.imageView6.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        } else {
            holder.postPreviewBinding.imageView6.setImageResource(R.drawable.ic_outline_thumb_up_24)
        }

        if (imageCount == 0) {
            holder.postPreviewBinding.postPreviewTailImageCount.visibility = View.GONE
        } else {
            holder.postPreviewBinding.postPreviewTailImageCount.visibility = View.VISIBLE
        }

        viewModel.isPostMorePreventDouble.observe(lifecycleOwner, androidx.lifecycle.Observer { it ->
            it?.let {
                holder.postPreviewBinding.postPreviewMoreImageView.isClickable = true
            }
        })

        holder.postPreviewBinding.postPreviewMoreImageView.setOnClickListener {
            viewModel.clickPostMore(position)
            it.isClickable = false
        }

        holder.postPreviewBinding.imageView6.setOnClickListener {
            if (mLastClickTime.toInt() == 0) {
                it.isActivated = false
                viewModel.clickPostLikeInCommunityContents(position)
            } else {
                it.isActivated = true
            }
            mLastClickTime = SystemClock.elapsedRealtime()
        }

        Glide.with(holder.postPreviewBinding.root.previewPostProfileImageView.context)
            .load(getItem(position).userProfileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.postPreviewBinding.root.previewPostProfileImageView)
    }

}