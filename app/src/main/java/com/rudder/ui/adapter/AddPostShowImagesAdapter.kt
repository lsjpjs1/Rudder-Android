package com.rudder.ui.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.FileInfo
import com.rudder.databinding.AddPostImagesBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.util.AddPostImagesDiffCallback
import com.rudder.util.AddPostImagesOnclickListener
import com.rudder.util.CategoriesDiffCallback
import com.rudder.util.CustomOnclickListener

class AddPostShowImagesAdapter(var imageUriList: ArrayList<FileInfo>,val displaySize:ArrayList<Int>,val addPostImagesOnclickListener: AddPostImagesOnclickListener) :
    RecyclerView.Adapter<AddPostShowImagesAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val addPostImagesBinding: AddPostImagesBinding) :
        RecyclerView.ViewHolder(addPostImagesBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPostShowImagesAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<AddPostImagesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.add_post_images,
            parent,
            false
        )

        return CustomViewHolder(bind)
    }

    override fun onBindViewHolder(
        holder: AddPostShowImagesAdapter.CustomViewHolder,
        position: Int
    ) {
        if (position == itemCount-1){
            holder.addPostImagesBinding.addPostImageView.also {
                it.layoutParams.height = 120
                it.layoutParams.width = 120
                it.setImageResource(R.drawable.ic_baseline_image_24)
                it.setOnClickListener{
                    addPostImagesOnclickListener.onClickAddImage(holder.addPostImagesBinding.root,position)
                }
            }
            holder.addPostImagesBinding.addPostDeleteButtonImageView.visibility= View.GONE
        }else{
            holder.addPostImagesBinding.addPostDeleteButtonImageView.also {
                it.visibility= View.VISIBLE
                it.setOnClickListener {
                    addPostImagesOnclickListener.onClickDeleteImage(holder.addPostImagesBinding.root,position)
                }
            }
            holder.addPostImagesBinding.addPostImageView.also {
                it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                it.setImageURI(imageUriList[position].uri)
                it.setOnClickListener {  }
            }
        }
    }

    override fun getItemCount(): Int {
        return imageUriList.size+1
    }


    fun updateImageUri(newUriList: ArrayList<FileInfo>) {

        val diffCallback: AddPostImagesDiffCallback =
            AddPostImagesDiffCallback(imageUriList, newUriList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        imageUriList.clear()
        imageUriList.addAll(newUriList)

        diffResult.dispatchUpdatesTo(this)
    }
}