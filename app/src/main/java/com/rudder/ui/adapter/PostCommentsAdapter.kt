package com.rudder.ui.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.SystemClock
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.data.Comment
import com.rudder.databinding.PostCommentsBinding
import com.rudder.util.CommentsDiffCallback
import com.rudder.util.LocaleUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.post_comments.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class PostCommentsAdapter(
    var commentList: ArrayList<Comment>,
    val context: Context,
    val viewModel: MainViewModel,
    val lifecycleOwner: LifecycleOwner,
): RecyclerView.Adapter<PostCommentsAdapter.CustomViewHolder>() {

    private var lastReplyClickPosition = -1
    private var replyClickCount = 0

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
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(context)).format(Date(commentList[position].postTime.time))
        holder.postCommentsBinding.mainVM = viewModel
        holder.postCommentsBinding.comment = commentList[position]
        holder.postCommentsBinding.timeago = timeago
        holder.postCommentsBinding.position = position
        var mLastClickTime: Long = 0

        if(commentList[position].status=="child"){
            holder.postCommentsBinding.nestedCommentImage.visibility=View.VISIBLE
            holder.postCommentsBinding.postPreviewTailCommentCount.visibility=View.GONE
            holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.light_grey,null)
            holder.postCommentsBinding.constraintLayout11.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        holder.postCommentsBinding.nestedCommentImage.minHeight = holder.postCommentsBinding.constraintLayout11.height
                        val lp = holder.postCommentsBinding.constraintLayout14.layoutParams as ConstraintLayout.LayoutParams
                        lp.startToStart = ConstraintLayout.LayoutParams.UNSET
                        holder.postCommentsBinding.constraintLayout14.layoutParams=lp

                        holder.postCommentsBinding.constraintLayout11.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )
        } else if(commentList[position].userId == ""){ // reply가 있는 삭제된 parent, Comment
            holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.light_grey_2,null)
            holder.postCommentsBinding.textView5.setTypeface(holder.postCommentsBinding.textView5.typeface, Typeface.ITALIC)
            holder.postCommentsBinding.commentHeadPostTimeTV.visibility = View.GONE
            holder.postCommentsBinding.postPreviewTailLikeCount.visibility = View.GONE
            holder.postCommentsBinding.postPreviewTailCommentCount.visibility = View.GONE
            holder.postCommentsBinding.CommentMoreCL.visibility = View.GONE
            holder.postCommentsBinding.nestedCommentImage.visibility = View.GONE
        } else { // 보통의 comment
            holder.postCommentsBinding.postPreviewTailCommentCount.setOnClickListener {
                val bindingPosition = holder.bindingAdapterPosition
                if (replyClickCount == 0) {
                    viewModel.clickNestedCommentReply(commentList[bindingPosition].groupNum, commentList[bindingPosition].commentBody)
                    holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.purple_100,null)
                    holder.postCommentsBinding.replyCloseButton.visibility = View.VISIBLE
                    replyClickCount += 1
                } else {
                    if (lastReplyClickPosition == bindingPosition) {
                        viewModel.clickNestedCommentReply(commentList[bindingPosition].groupNum, commentList[bindingPosition].commentBody)
                        holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.purple_100,null)
                        holder.postCommentsBinding.replyCloseButton.visibility = View.VISIBLE
                        lastReplyClickPosition = bindingPosition
                        replyClickCount += 1
                    }
                }
            }

            holder.postCommentsBinding.replyCloseButton.setOnClickListener {
                holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.white,null)
                holder.postCommentsBinding.replyCloseButton.visibility = View.GONE
                viewModel.clearNestedCommentInfo()
                holder.postCommentsBinding.postPreviewTailCommentCount.isClickable = true
                replyClickCount = 0
            }

            viewModel.selectedCommentGroupNum.observe(lifecycleOwner, androidx.lifecycle.Observer {
                if (it == -1) {
                    holder.postCommentsBinding.eachComment.background=ResourcesCompat.getDrawable(context.resources,R.color.white,null)
                    holder.postCommentsBinding.replyCloseButton.visibility = View.GONE
                    replyClickCount = 0
                }
            })
        }


        holder.postCommentsBinding.commentMoreImageView.setOnClickListener {
            viewModel.clickCommentMore(position)
            it.isClickable = false
        }

        viewModel.isCommentMorePreventDouble.observe(lifecycleOwner, androidx.lifecycle.Observer { it ->
            it?.let {
                holder.postCommentsBinding.commentMoreImageView.isClickable = true
            }

        })

        holder.postCommentsBinding.postPreviewTailLikeTV.setOnClickListener {
            if (mLastClickTime.toInt() == 0) {
                it.isActivated = false
                viewModel.clickCommentLike(position)
            } else {
                it.isActivated = true
            }

            mLastClickTime = SystemClock.elapsedRealtime()
        }

        Glide.with(holder.postCommentsBinding.commentProfileImageView.context)
            .load(commentList[position].userProfileImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.postCommentsBinding.commentProfileImageView)

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun updateComments(newComments: ArrayList<Comment>, deleteFlag : Boolean){
        if(newComments.size>=0) {
            val diffCallback: CommentsDiffCallback = CommentsDiffCallback(commentList, newComments, deleteFlag)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            commentList.clear()
            commentList.addAll(newComments)
            diffResult.dispatchUpdatesTo(this)
        }
    }


}