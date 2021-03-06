package com.rudder.ui.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.PreviewPost
import com.rudder.databinding.PostPreviewBinding
import com.rudder.util.CustomOnclickListener
import com.rudder.util.PostsDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

abstract class PostPreviewAdapter<out VM>(
    val listener: CustomOnclickListener,
    val context: Context,
    val viewModel: VM,
    val lifecycleOwner: LifecycleOwner
) : ListAdapter<PreviewPost,PostPreviewAdapter<out VM>.CustomViewHolder>(diffUtil) {
    val MAX_POST_BODY_LENGTH = 50

    inner class CustomViewHolder(val postPreviewBinding: PostPreviewBinding) :
        RecyclerView.ViewHolder(postPreviewBinding.root)

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<PreviewPost>() {
            override fun areContentsTheSame(oldItem: PreviewPost, newItem: PreviewPost): Boolean {
                return (oldItem.commentCount == newItem.commentCount
                        && oldItem.likeCount == newItem.likeCount)
            }


            override fun areItemsTheSame(oldItem: PreviewPost, newItem: PreviewPost): Boolean {
                return oldItem.postId == newItem.postId
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostPreviewAdapter<out VM>.CustomViewHolder {
        val bind = DataBindingUtil.inflate<PostPreviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.post_preview,
            parent,
            false
        )

        return CustomViewHolder(bind)
    }



}