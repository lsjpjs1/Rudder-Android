package com.rudder.ui.fragment.post


import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentShowPostContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.DisplayImagesAdapter
import com.rudder.ui.adapter.PostCommentsAdapter
import com.rudder.ui.fragment.comment.CommunityCommentBottomSheetFragment
import com.rudder.util.CirclePagerIndicatorDecoration
import com.rudder.util.CustomOnclickListener
import com.rudder.util.LocaleUtil
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_show_post_contents.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class ShowPostContentsFragment() : Fragment(), CustomOnclickListener {

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }
    private val viewModel: MainViewModel by lazy {
        (parentFragment as ShowPostDisplayFragment).viewModel
    }
    var _fragmentBinding: FragmentShowPostContentsBinding? = null
    val fragmentBinding get() = _fragmentBinding!!

    private val purpleRudder by lazy {
        ContextCompat.getColor(
            lazyContext!!,
            R.color.purple_rudder
        )
    }

    companion object {
        const val TAG = "ShowPostContentsFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentBinding = FragmentShowPostContentsBinding.inflate(inflater, container, false)

        val viewModelType = viewModel.javaClass.name.split('.').last()
        val adapter = PostCommentsAdapter(
            viewModel.comments.value!!,
            lazyContext,
            viewModel,
            parentFragment as LifecycleOwner
        )
        val displayImagesAdapter: DisplayImagesAdapter

        fragmentBinding.commentDisplayRV.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = adapter
        }


        viewModel.isLikePost()



        if (viewModelType == "NotificationViewModel") {
            if (viewModel.posts.value!![0].imageUrls.size == 0) {
                fragmentBinding.showPostImageDisplay.visibility = View.GONE
            } else {
                fragmentBinding.showPostImageDisplay.visibility = View.VISIBLE
            }
            displayImagesAdapter = DisplayImagesAdapter(
                viewModel.posts.value!![0].imageUrls,
                lazyContext,
                (activity as MainActivity).getDisplaySize(),
                this
            )
        } else {
            if (viewModel.posts.value!![viewModel.selectedPostPosition.value!!].imageUrls.size == 0) {
                fragmentBinding.showPostImageDisplay.visibility = View.GONE
            } else {
                fragmentBinding.showPostImageDisplay.visibility = View.VISIBLE
            }
            displayImagesAdapter = DisplayImagesAdapter(
                viewModel.posts.value!![viewModel.selectedPostPosition.value!!].imageUrls,
                lazyContext,
                (activity as MainActivity).getDisplaySize(),
                this
            )
        }

        fragmentBinding.showPostImageDisplayRecyclerView.also {
            it.layoutManager = LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = displayImagesAdapter
        }
        val snapHelper = PagerSnapHelper()
        fragmentBinding.showPostImageDisplayRecyclerView.addItemDecoration(
            CirclePagerIndicatorDecoration()
        )
        snapHelper.attachToRecyclerView(fragmentBinding.showPostImageDisplayRecyclerView)

        setFragmentBindingPost()

        val timeago = PrettyTime(LocaleUtil().getSystemLocale(lazyContext)).format(Date(fragmentBinding.post!!.postTime.time))
        fragmentBinding.mainVM = viewModel
        fragmentBinding.position = viewModel.selectedPostPosition.value!!
        fragmentBinding.lifecycleOwner = this
        fragmentBinding.timeago = timeago

        //viewModel.getComments()    //????????? ??????, // Spring ?????? ?????????

        viewModel.comments.observe(parentFragment as LifecycleOwner, Observer {
            var deleteCommentflag = false
            viewModel.isDeleteCommentSuccess.value!!.getContentIfNotHandled()?.let {
                deleteCommentflag = it
            }
            adapter.updateComments(viewModel.comments.value!!, !deleteCommentflag)

            fragmentBinding.showPostContentsSwipeRefreshLayout.isRefreshing = false
        })

        viewModel.commentCountChange.observe(parentFragment as LifecycleOwner, Observer {
            setFragmentBindingPost()
        })

        viewModel.selectedPostPosition.observe(parentFragment as LifecycleOwner, Observer {
            setFragmentBindingPost()
            displayImagesAdapter.imageUrlList = fragmentBinding.post!!.imageUrls
            displayImagesAdapter.notifyDataSetChanged()
            Glide.with(fragmentBinding.showPostImageView.context)
                .load(fragmentBinding.post!!.userProfileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fragmentBinding.showPostImageView)
        })

        viewModel.postInnerValueChangeSwitch.observe(parentFragment as LifecycleOwner, Observer {
            it?.let {
                setFragmentBindingPost()
            }

        })

        viewModel.posts.observe(parentFragment as LifecycleOwner, Observer {
            it?.let {
                if (viewModel.selectedPostPosition.value!! < viewModel.posts.value!!.size) {
                    setFragmentBindingPost()
                }
            }
        })

        viewModel.isPostFromId.observe(parentFragment as LifecycleOwner, Observer {
            it?.let {
                fragmentBinding.post = viewModel.postFromId.value!!

            }
        })

        viewModel.commentInnerValueChangeSwitch.observe(parentFragment as LifecycleOwner, Observer {
            it?.let {
                viewModel.commentLikeCountChange.value?.let { position ->
                    adapter.notifyItemChanged(position)
                }
            }
        })

        viewModel.isShowPostRefreshSuccess.observe(parentFragment as LifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                setFragmentBindingPost()
            }
        })

        fragmentBinding.showPostBody.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    fixOtherViewHeight(fragmentBinding.showPostBody)
                    fragmentBinding.showPostBody.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )


        viewModel.isLikePost.observe(parentFragment as LifecycleOwner, Observer {
            if (it!!) {
                showPostLikeImageView?.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            } else {
                showPostLikeImageView?.setImageResource(R.drawable.ic_outline_thumb_up_24)
            }
        })

        viewModel.isPostMore.observe(parentFragment as LifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let { bool ->
                if (bool) {
                }
                (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
            }
        })

        viewModel.selectedParentCommentBody.observe(parentFragment as LifecycleOwner, Observer {
            it?.let {
                (activity as MainActivity).setParentCommentInfoText(it)
            }
        })

        viewModel.isCommentMore.observe(parentFragment as LifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { bool ->
                if (bool) {
                }
                (activity as MainActivity).showCommentMore(
                    CommunityCommentBottomSheetFragment(viewModel)
                )
            }
        })

        viewModel.selectedCommentGroupNum.observe(parentFragment as LifecycleOwner, Observer {
            if (it != -1) {
                (activity as MainActivity).showParentCommentInfo()
            } else {
                (activity as MainActivity).hideParentCommentInfo()
            }
        })


        fragmentBinding.postMoreImageView.setOnClickListener {
            viewModel.clickPostMore(viewModel.selectedPostPosition.value!!)
            it.isClickable = false
        }

        viewModel.isPostMorePreventDouble.observe(parentFragment as LifecycleOwner, Observer { it ->
            it?.let {
                fragmentBinding.postMoreImageView.isClickable = true
            }
        })


        viewModel.isEditPostSuccess.observe(parentFragment as LifecycleOwner, Observer { it ->
            it?.let {
                setFragmentBindingPost()
            }
        })


        viewModel.isPostDeleteShowPost.observe(parentFragment as LifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(context, "Delete Post Complete!", Toast.LENGTH_LONG).show()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                    parentActivity.onBackPressed()

                }
            }
        })

        viewModel.clearNestedCommentInfo()

        fragmentBinding.showPostContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        fragmentBinding.showPostContentsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.scrollTopShowPost()
        }


        viewModel.isBlockUserInComment.observe(parentFragment as LifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(context, "Block User Complete!", Toast.LENGTH_LONG).show()
                    parentActivity.communityCommentBottomSheetFragment.dismiss()
                    parentActivity.onBackPressed()
                    viewModel.clearPosts()
                    //viewModel.getPosts()
                    viewModel.getPostsFun()

                }
            }
        })



        return fragmentBinding.root
    }


    override fun onPause() {
        viewModel.clearNestedCommentInfo()
        super.onPause()
    }

    //????????? ??? ????????? ?????? ????????? view??? ????????? ?????????????????? ??????
    fun fixOtherViewHeight(showPostBody: View) {
        val showPostBodyHeight = showPostBody.height
        val typedValue = TypedValue()
        val typedValue2 = TypedValue()
        lazyContext.resources.getValue(R.dimen.post_info_height, typedValue, true)
        lazyContext.resources.getValue(R.dimen.divide_default, typedValue2, true)
        val postInfoHeightRatio = typedValue.float
        val divideHeightRatio = typedValue2.float
        //????????????????????? ????????? ????????? ?????? ?????? ??????
        var lp = constraintLayout16.layoutParams
        lp.height = (showPostBodyHeight * divideHeightRatio).toInt()
        constraintLayout16.layoutParams = lp

        lp = constraintLayout8.layoutParams
        lp.height = (showPostBodyHeight * postInfoHeightRatio).toInt()
        constraintLayout8.layoutParams = lp

        lp = constraintLayout9.layoutParams
        lp.height = (showPostBodyHeight * postInfoHeightRatio).toInt()
        constraintLayout9.layoutParams = lp

        lp = constraintLayout10.layoutParams
        lp.height = (showPostBodyHeight * 0.065).toInt()
        constraintLayout10.layoutParams = lp
    }


    fun setFragmentBindingPost() {
        if (viewModel.selectedPostPosition.value!! == -1) {
            fragmentBinding.post = viewModel.postFromId.value!!
        } else {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        }
    }

    override fun onClickView(view: View, position: Int) {
        var imageSliderDialogFragment : ImageSliderDialogFragment
        if (viewModel is NotificationViewModel) {
            imageSliderDialogFragment = ImageSliderDialogFragment.instance(viewModel.posts.value!![0].imageUrls,position)
        } else {
            imageSliderDialogFragment = ImageSliderDialogFragment.instance(viewModel.posts.value!![viewModel.selectedPostPosition.value!!].imageUrls,position)
        }
        imageSliderDialogFragment.show(childFragmentManager, ImageSliderDialogFragment.TAG)
    }

}