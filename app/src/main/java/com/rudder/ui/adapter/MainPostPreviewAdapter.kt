package com.rudder.ui.adapter

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.PreviewPost
import com.rudder.util.CustomOnclickListener
import com.rudder.util.LocaleUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.post_preview.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class MainPostPreviewAdapter(listener:CustomOnclickListener,
                             context:Context,
                             viewModel:MainViewModel
) : PostPreviewAdapter<MainViewModel>(listener,context,viewModel) {
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val timeago =
            PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(getItem(position).postTime.time))

        val imageCount = getItem(position).imageUrls.size

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
            listener.onClick(holder.postPreviewBinding.postPreview, position)
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



        Glide.with(holder.postPreviewBinding.root.previewPostProfileImageView.context)
            .load(getItem(position).userProfileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.postPreviewBinding.root.previewPostProfileImageView)
    }

}