package com.rudder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.rudder.R
import com.rudder.data.Post
import com.rudder.databinding.PostPreviewBinding

class PostPreviewAdapter(val postList: ArrayList<Post>): RecyclerView.Adapter<PostPreviewAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(val postPreviewBinding: PostPreviewBinding) : RecyclerView.ViewHolder(postPreviewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostPreviewAdapter.CustomViewHolder{
        val holder = CustomViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.post_preview,
                parent,
                false
            )
        )
//        val params = holder.itemView.layoutParams as Cons
//        params.height = 300
//        holder.itemView.layoutParams = params
        return holder
    }


    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.postPreviewBinding.post=postList[position]
    }

}