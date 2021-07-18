package com.rudder.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.Post
import com.rudder.databinding.PostPreviewBinding
import com.rudder.util.CustomOnclickListener
import com.rudder.util.PostsDiffCallback

class PostPreviewAdapter(val postList: ArrayList<Post>,val listener: CustomOnclickListener): RecyclerView.Adapter<PostPreviewAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(val postPreviewBinding: PostPreviewBinding) : RecyclerView.ViewHolder(postPreviewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostPreviewAdapter.CustomViewHolder{
        val bind = DataBindingUtil.inflate<PostPreviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.post_preview,
            parent,
            false
        )
        val params = bind.root.layoutParams
        params.height = 700 // 아이템뷰 높이 고정값으로 되어있는 것 상대값으로 수정해야함
        bind.root.layoutParams = params
        return CustomViewHolder(bind)
    }


    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.postPreviewBinding.post=postList[position]
        holder.postPreviewBinding.postPreview.setOnClickListener{
            listener.onPostPreviewClick(holder.postPreviewBinding.postPreview,position)
        }
    }

    fun updatePosts(newPosts: ArrayList<Post>){
        if(newPosts.size>0) {
            val diffCallback: PostsDiffCallback = PostsDiffCallback(postList, newPosts)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            postList.clear()
            postList.addAll(newPosts)
            diffResult.dispatchUpdatesTo(this)
        }
    }


}