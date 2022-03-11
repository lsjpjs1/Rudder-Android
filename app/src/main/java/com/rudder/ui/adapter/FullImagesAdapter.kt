package com.rudder.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.dto.PostMessageRoom
import com.rudder.databinding.FullImageBinding
import kotlinx.android.synthetic.main.edit_profile_images.view.*
import kotlinx.android.synthetic.main.full_image.view.*

class FullImagesAdapter : BaseAdapter<String, FullImageBinding>(diffUtil,R.layout.full_image) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        Glide.with(holder.viewBinding.root.context)
            .load(getItem(position))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(holder.viewBinding.root.fullImagePhotoView)
    }
}