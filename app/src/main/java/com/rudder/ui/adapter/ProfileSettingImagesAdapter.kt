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
import com.rudder.databinding.FragmentProfileSettingBinding
import kotlinx.android.synthetic.main.profile_setting_display_image.view.*



class ProfileSettingImagesAdapter(
    var imageUrlList: ArrayList<String>,
    val context: Context,
    val displaySize: ArrayList<Int>
): RecyclerView.Adapter<ProfileSettingImagesAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val fragmentProfileSettingBinding: FragmentProfileSettingBinding) : RecyclerView.ViewHolder(
        fragmentProfileSettingBinding.root
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileSettingImagesAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<FragmentProfileSettingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.profile_setting_display_image,
            parent,
            false
        )

        var lp = bind.root.profileSettingImageImageView.layoutParams
        lp.height = (displaySize[1]*0.4).toInt()
        bind.root.profileSettingImageImageView.layoutParams=lp
        return CustomViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ProfileSettingImagesAdapter.CustomViewHolder, position: Int) {
        Glide.with(holder.fragmentProfileSettingBinding.root.context)
            .load(imageUrlList[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.raw.post_loading_raw)
            .into(holder.fragmentProfileSettingBinding.root.profileSettingImageImageView)

    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }
}