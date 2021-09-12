package com.rudder.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.ProfileSettingDisplayImageBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_profile_setting.*
import kotlinx.android.synthetic.main.profile_setting_display_image.view.*


class ProfileSettingImagesAdapter(
    var imageUrlList: ArrayList<String>,
    val context: Context,
    val displaySize: ArrayList<Int>,
    val viewModel: SignUpViewModel,
    val lifecycleOwner: LifecycleOwner
    ): RecyclerView.Adapter<ProfileSettingImagesAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val profileSettingDisplayImageBinding: ProfileSettingDisplayImageBinding) : RecyclerView.ViewHolder(
        profileSettingDisplayImageBinding.root
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileSettingImagesAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<ProfileSettingDisplayImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.profile_setting_display_image,
            parent,
            false
        )

        var lp = bind.root.profileSettingImageImageView.layoutParams
        lp.height = (displaySize[1]*0.15).toInt()
        lp.width = (displaySize[0]*0.2).toInt()
        bind.root.profileSettingImageImageView.layoutParams=lp

        return CustomViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ProfileSettingImagesAdapter.CustomViewHolder, position: Int) {

        Glide.with(holder.profileSettingDisplayImageBinding.root.context)
            .load(imageUrlList[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.raw.post_loading_raw)
            .into(holder.profileSettingDisplayImageBinding.root.profileSettingImageImageView)



//        if(holder.profileSettingDisplayImageBinding.profileSettingImageCheckBox.isChecked == true) {
//            Log.d("checkbox","${position}")
//        }


        viewModel.selectedProfileImage.observe(lifecycleOwner, Observer {
            it?.let{
                Log.d("asd","asd")
                holder.profileSettingDisplayImageBinding.profileSettingImageCheckBox.isChecked = it == position

            }
        })



        holder.profileSettingDisplayImageBinding.signUpVM = viewModel
        holder.profileSettingDisplayImageBinding.position = position
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