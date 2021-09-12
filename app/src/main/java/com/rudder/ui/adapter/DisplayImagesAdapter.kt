package com.rudder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.ShowPostDisplayImageBinding
import kotlinx.android.synthetic.main.show_post_display_image.view.*


class DisplayImagesAdapter(
    var imageUrlList: ArrayList<String>,
    val context: Context,
    val displaySize: ArrayList<Int>
): RecyclerView.Adapter<DisplayImagesAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val showPostDisplayImageBinding: ShowPostDisplayImageBinding) : RecyclerView.ViewHolder(
        showPostDisplayImageBinding.root
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DisplayImagesAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<ShowPostDisplayImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.show_post_display_image,
            parent,
            false
        )

        var lp = bind.root.showPostDisplayImageImageView.layoutParams
        lp.height = (displaySize[1]*0.4).toInt()
        bind.root.showPostDisplayImageImageView.layoutParams=lp
        return CustomViewHolder(bind)
    }

    override fun onBindViewHolder(holder: DisplayImagesAdapter.CustomViewHolder, position: Int) {
        Glide.with(holder.showPostDisplayImageBinding.root.context)
            .load(imageUrlList[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.raw.post_loading_raw)
            .into(holder.showPostDisplayImageBinding.root.showPostDisplayImageImageView)

    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}