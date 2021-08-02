package com.rudder.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.data.Post
import com.rudder.databinding.PostCommentsBinding
import com.rudder.util.CommentsDiffCallback
import com.rudder.util.LocaleUtil
import com.rudder.util.PostsDiffCallback
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class PostCommentsAdapter(val commentList: ArrayList<Comment>,val context: Context): RecyclerView.Adapter<PostCommentsAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val postCommentsBinding: PostCommentsBinding) : RecyclerView.ViewHolder(postCommentsBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentsAdapter.CustomViewHolder {
        val bind = DataBindingUtil.inflate<PostCommentsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.post_comments,
                parent,
                false
        )
        val params = bind.root.layoutParams
        val typedValue = TypedValue()
        context.resources.getValue(R.dimen.comment_height, typedValue,true)
        val heightRatio = typedValue.float
        params.height = (parent.height * heightRatio).toInt() // 아이템뷰 높이 고정값으로 되어있는 것 상대값으로 수정해야함
        bind.root.layoutParams = params
        return CustomViewHolder(bind)
    }

    override fun onBindViewHolder(holder: PostCommentsAdapter.CustomViewHolder, position: Int) {
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(commentList[position].postTime.time))
        holder.postCommentsBinding.comment = commentList[position]
        holder.postCommentsBinding.timeago = timeago
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun updateComments(newComments: ArrayList<Comment>){
        if(newComments.size>0) {
            val diffCallback: CommentsDiffCallback = CommentsDiffCallback(commentList, newComments)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            commentList.clear()
            commentList.addAll(newComments)
            diffResult.dispatchUpdatesTo(this)
        }
    }

}