package com.rudder.ui.adapter

import android.annotation.SuppressLint
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
import com.rudder.util.LocaleUtil
import com.rudder.util.PostsDiffCallback
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class PostPreviewAdapter(
    val previewPostList: ArrayList<PreviewPost>,
    val listener: CustomOnclickListener,
    val context: Context
) : RecyclerView.Adapter<PostPreviewAdapter.CustomViewHolder>() {
    private val MAX_POST_BODY_LENGTH = 50

    inner class CustomViewHolder(val postPreviewBinding: PostPreviewBinding) :
        RecyclerView.ViewHolder(postPreviewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostPreviewAdapter.CustomViewHolder {
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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val timeago =
            PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(previewPostList[position].postTime.time))
        holder.postPreviewBinding.post = previewPostList[position]
        holder.postPreviewBinding.timeago = timeago
        holder.postPreviewBinding.also {
            it.post = previewPostList[position]
            it.timeago = timeago
            it.maxpostbodylength = MAX_POST_BODY_LENGTH
        }
        holder.postPreviewBinding.postPreview.setOnClickListener {
            listener.onClick(holder.postPreviewBinding.postPreview, position)
        }
        if(previewPostList[position].isLiked){
            holder.postPreviewBinding.imageView6.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        }else{
            holder.postPreviewBinding.imageView6.setImageResource(R.drawable.ic_outline_thumb_up_24)
        }
    }

    fun updatePosts(newPreviewPosts: ArrayList<PreviewPost>) {

        val diffCallback: PostsDiffCallback = PostsDiffCallback(previewPostList, newPreviewPosts)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        previewPostList.clear()
        previewPostList.addAll(newPreviewPosts)
        diffResult.dispatchUpdatesTo(this)

    }


}