package com.rudder.ui.fragment.post


import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentShowPostContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.DisplayImagesAdapter
import com.rudder.ui.adapter.PostCommentsAdapter
import com.rudder.ui.fragment.comment.CommunityCommentBottomSheetFragment
import com.rudder.util.ProgressBarUtil
import com.rudder.util.LocaleUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_show_post_contents.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class ShowPostContentsFragment(): Fragment() {
    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }


    private val viewModel : MainViewModel by activityViewModels()

    private var _fragmentBinding : FragmentShowPostContentsBinding? = null
    private val fragmentBinding get() = _fragmentBinding!!



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //_fragmentBinding= DataBindingUtil.inflate<FragmentShowPostContentsBinding>(inflater,R.layout.fragment_show_post_contents,container,false)

        _fragmentBinding= FragmentShowPostContentsBinding.inflate(inflater, container, false)


        val adapter = PostCommentsAdapter(viewModel.comments.value!!,lazyContext,viewModel)
        fragmentBinding.commentDisplayRV.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        Log.d("showpost","${viewModel.posts.value!!}")

        val displayImagesAdapter = DisplayImagesAdapter(viewModel.posts.value!![viewModel.selectedPostPosition.value!!].imageUrls,lazyContext,(activity as MainActivity).getDisplaySize())

        fragmentBinding.showPostImageDisplayRecyclerView.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = displayImagesAdapter
        }
        val currentPost = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        val timeago = PrettyTime(LocaleUtil().getSystemLocale(lazyContext)).format(Date(currentPost.postTime.time))
        fragmentBinding.post = currentPost
        fragmentBinding.mainVM = viewModel
        fragmentBinding.position = viewModel.selectedPostPosition.value!!
        fragmentBinding.lifecycleOwner = this
        fragmentBinding.timeago = timeago



        viewModel.comments.observe(viewLifecycleOwner, Observer {
            var deleteCommentflag = false
            viewModel.isDeleteCommentSuccess.value!!.getContentIfNotHandled()?.let{
                deleteCommentflag = it
            }
            adapter.updateComments(viewModel.comments.value!!, !deleteCommentflag)
        })

        viewModel.commentCountChange.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        })

        viewModel.selectedPostPosition.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
            displayImagesAdapter.imageUrlList = viewModel.posts.value!![it].imageUrls
            displayImagesAdapter.notifyDataSetChanged()
            Glide.with(fragmentBinding.showPostImageView.context)
                .load(currentPost.userProfileImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fragmentBinding.showPostImageView)
        })
        viewModel.postInnerValueChangeSwitch.observe(viewLifecycleOwner, Observer {
            it?.let {
                    fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
            }

        })

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (viewModel.selectedPostPosition.value!!<viewModel.posts.value!!.size){
                    fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
                }

            }
        })

        viewModel.commentInnerValueChangeSwitch.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.commentLikeCountChange.value?.let {
                    position-> adapter.notifyItemChanged(position)
                }
            }
        })



        childFragmentManager.beginTransaction()
                //.add(R.id.showPostHeader, ShowPostHeaderFragment(viewModel))
                .commit()


        fragmentBinding.showPostBody.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        fixOtherViewHeight(fragmentBinding.showPostBody)
                        fragmentBinding.showPostBody.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
        )


        ProgressBarUtil.progressBarFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it)
                    ProgressBarUtil.progressBarVisibleFragment(progressBarShowPost, this)
                else
                    ProgressBarUtil.progressBarGoneFragment(progressBarShowPost, this)
            }
        })

        viewModel.isLikePost.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                showPostLikeImageView?.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            } else {
                showPostLikeImageView?.setImageResource(R.drawable.ic_outline_thumb_up_24)
            }
        })

        viewModel.isPostMore.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                bool ->
                if(bool)
                (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
            }

//            it?.let {
//                if(it)
//                (activity as MainActivity).showPostMore(CommunityPostBottomSheetFragment(viewModel))
//            }

        })

        viewModel.selectedParentCommentBody.observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as MainActivity).setParentCommentInfoText(it)
                //fragmentBinding.parentCommentInfoTextTextView.text = it

            }
        })

        viewModel.isCommentMore.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                    bool ->
                if(bool)
                (activity as MainActivity).showCommentMore(CommunityCommentBottomSheetFragment(viewModel))
            }
        })

        viewModel.selectedCommentGroupNum.observe(viewLifecycleOwner, Observer {
            Log.d("selectedgroupnum",it.toString())
            if (it != -1) {
                (activity as MainActivity).showParentCommentInfo()
            } else {
                (activity as MainActivity).hideParentCommentInfo()
            }
        })



//        fragmentBinding.parentCommentInfoClose.setOnClickListener {
//            viewModel.clearNestedCommentInfo()
//            tmpBack()
//        }


        return fragmentBinding.root
    }

//    val postMoreOnclickListener = object : View.OnClickListener{
//        override fun onClick(p0: View?) {
//            val popupMenu = PopupMenu(lazyContext,view)
//            MenuInflater(lazyContext).inflate(R.menu.post_pop_up_menu,popupMenu.menu)
//            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
//                override fun onMenuItemClick(p0: MenuItem?): Boolean {
//                    if (p0 != null) {
//                        when (p0.itemId){
//                            R.id.post_edit->Log.d("edit","edit")
//                            R.id.post_delete->Log.d("delete","delete")
//                        }
//
//                    }else{
//                        return false
//                    }
//                    return false
//                }
//            })
//            popupMenu.show()
//        }
//
//    }

    override fun onPause() {
        viewModel.clearNestedCommentInfo()
        super.onPause()
    }



    //스크롤 뷰 높이에 따라 바뀌는 view의 높이를 고정시켜주는 함수
   fun fixOtherViewHeight(showPostBody: View){
        val showPostBodyHeight = showPostBody.height
        val typedValue = TypedValue()
        val typedValue2 = TypedValue()
        lazyContext.resources.getValue(R.dimen.post_info_height, typedValue,true)
        lazyContext.resources.getValue(R.dimen.divide_default, typedValue2,true)
        val postInfoHeightRatio = typedValue.float
        val divideHeightRatio = typedValue2.float

        //리사이클러뷰를 제외한 나머지 뷰의 높이 고정
        var lp = constraintLayout16.layoutParams
        lp.height=(showPostBodyHeight*divideHeightRatio).toInt()
        constraintLayout16.layoutParams=lp

        lp=constraintLayout8.layoutParams
        lp.height=(showPostBodyHeight*postInfoHeightRatio).toInt()
        constraintLayout8.layoutParams=lp

        lp=constraintLayout9.layoutParams
        lp.height=(showPostBodyHeight*postInfoHeightRatio).toInt()
        constraintLayout9.layoutParams=lp

        lp=constraintLayout10.layoutParams
        lp.height=(showPostBodyHeight*postInfoHeightRatio).toInt()
        constraintLayout10.layoutParams=lp



    }

    fun closeParentCommentInfo(){
        viewModel.clearNestedCommentInfo()
    }


//    fun tmp(){
//        fragmentBinding.parentCommentInfo.visibility = View.VISIBLE
//    }
//
//
//    fun tmpBack() {
//        fragmentBinding.parentCommentInfo.visibility = View.GONE
//    }
//
//    fun tmp3() {
//        viewModel.clearNestedCommentInfo()
//    }

}