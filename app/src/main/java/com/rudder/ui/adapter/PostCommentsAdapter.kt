package com.rudder.ui.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.databinding.PostCommentsBinding
import com.rudder.util.CommentsDiffCallback
import com.rudder.util.LocaleUtil
import kotlinx.android.synthetic.main.post_comments.view.*
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
        val showPostBodyHeight by lazy {
            (context as Activity).findViewById<ConstraintLayout>(R.id.showPostBody).height
        }
        val params = bind.root.layoutParams
        val typedValue = TypedValue()
        val typedValue2 = TypedValue()
        context.resources.getValue(R.dimen.comment_height, typedValue,true)
        context.resources.getValue(R.dimen.comment_header_height, typedValue2,true)
        bind.root.textView5.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.AT_MOST),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val entireHeightRatio = typedValue.float
        val headerHeightRatio = typedValue2.float
        val headerHeight = (showPostBodyHeight * headerHeightRatio).toInt()

        var lp =bind.root.constraintLayout11.layoutParams
        lp.height=headerHeight
        bind.root.constraintLayout11.layoutParams=lp

        lp = bind.root.postPreviewTail.layoutParams
        lp.height=headerHeight
        bind.root.postPreviewTail.layoutParams=lp



        return CustomViewHolder(bind)
    }



    override fun onBindViewHolder(holder: PostCommentsAdapter.CustomViewHolder, position: Int) {
        if(commentList[position].status=="child"){
            holder.postCommentsBinding.nestedCommentImage.visibility=View.VISIBLE
            holder.postCommentsBinding.constraintLayout11.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener{
                        override fun onGlobalLayout() {
                            Log.d("position$position",holder.postCommentsBinding.constraintLayout11.height.toString())
                            holder.postCommentsBinding.nestedCommentImage.minHeight = holder.postCommentsBinding.constraintLayout11.height
                            val lp = holder.postCommentsBinding.constraintLayout14.layoutParams as ConstraintLayout.LayoutParams
                            lp.startToStart = ConstraintLayout.LayoutParams.UNSET
                            holder.postCommentsBinding.constraintLayout14.layoutParams=lp

                            holder.postCommentsBinding.constraintLayout11.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }

                    }
            )
        }


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