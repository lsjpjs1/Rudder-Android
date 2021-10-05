package com.rudder.ui.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
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
    val previewPostList: ArrayList<PreviewPost>,
    val listener: CustomOnclickListener,
    val context: Context,
    val viewModel: VM
) : RecyclerView.Adapter<PostPreviewAdapter<out VM>.CustomViewHolder>() {
    val MAX_POST_BODY_LENGTH = 50

    inner class CustomViewHolder(val postPreviewBinding: PostPreviewBinding) :
        RecyclerView.ViewHolder(postPreviewBinding.root)

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
        val params = bind.root.layoutParams
        val typedValue = TypedValue()
        context.resources.getValue(R.dimen.post_preview_height, typedValue, true)
        val heightRatio = typedValue.float
        params.height = (parent.height * heightRatio).toInt() // 아이템뷰 높이 고정값으로 되어있는 것 상대값으로 수정해야함
        bind.root.layoutParams = params
        return CustomViewHolder(bind)
    }


    override fun getItemCount(): Int {
        return previewPostList.size
    }

    fun updatePosts(newPreviewPosts: ArrayList<PreviewPost>) {
            Log.d("previewPostList",previewPostList.toString())
            Log.d("newPostList",newPreviewPosts.toString())
            val diffCallback: PostsDiffCallback = PostsDiffCallback()
            diffCallback.newList = ArrayList(newPreviewPosts)
            diffCallback.oldList = ArrayList(previewPostList)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
                previewPostList.clear()
                previewPostList.addAll(newPreviewPosts)
                diffResult.dispatchUpdatesTo(this@PostPreviewAdapter)

        }





}