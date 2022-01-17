package com.rudder.ui.adapter

import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.DisplaySize
import com.rudder.data.dto.ProfileImage
import com.rudder.databinding.EditProfileImagesBinding
import com.rudder.ui.fragment.mypage.EditProfileImageDialogFragment
import kotlinx.android.synthetic.main.edit_profile_images.view.*

class EditProfileImagesAdapter(
    val displaySize: DisplaySize,
    val parentFragment: EditProfileImageDialogFragment

) : BaseAdapter<ProfileImage,EditProfileImagesBinding>(diffUtil, R.layout.edit_profile_images) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProfileImage>() {
            override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
                return (oldItem.profileImageId == newItem.profileImageId) && (oldItem.previewLink == newItem.previewLink)
            }

            override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
                return oldItem.profileImageId == newItem.profileImageId //수정 해야됨
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val superHolder = super.onCreateViewHolder(parent, viewType)
        val lp = superHolder.viewBinding.root.layoutParams
        lp.height = (displaySize.height*0.15).toInt()
        lp.width = (displaySize.width*0.2).toInt()
        superHolder.viewBinding.root.layoutParams = lp
        return superHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        Glide.with(holder.viewBinding.root.context)
            .load(getItem(position).previewLink)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(holder.viewBinding.root.editProfileImageImageView)
        holder.viewBinding.editProfileImageImageView.setOnClickListener {
            parentFragment.viewModel.clickProfileImage(getItem(position))
        }

        parentFragment.viewModel._selectedProfileImage.observe(parentFragment.viewLifecycleOwner, Observer {
            it?.let {
                holder.viewBinding.editProfileImageCheckBox.isChecked =
                    (it.profileImageId==getItem(position).profileImageId)
            }
        })

    }
}