package com.rudder.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentShowPostBinding
import com.rudder.ui.adapter.PostCommentsAdapter
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_show_post.*
import kotlinx.android.synthetic.main.fragment_show_post.view.*

class ShowPostFragment: Fragment() {
    private val viewModel :MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding= DataBindingUtil.inflate<FragmentShowPostBinding>(inflater,R.layout.fragment_show_post,container,false)

        val adapter = PostCommentsAdapter(viewModel.comments.value!!,lazyContext)
        fragmentBinding.commentDisplayRV.also {
            it.layoutManager = object : LinearLayoutManager(lazyContext){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = adapter
        }
        fragmentBinding.commentCount=viewModel.comments.value!!.size
        fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        fragmentBinding.mainVM = viewModel
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            adapter.updateComments(it)
            fragmentBinding.commentCount=viewModel.comments.value!!.size
        })
        viewModel.selectedPostPosition.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        })
        childFragmentManager.beginTransaction()
                .add(R.id.showPostHeader,ShowPostHeaderFragment())
                .commit()

        fragmentBinding.showPostBody.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        fixOtherViewHeight()
                        fragmentBinding.showPostBody.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }

                }
        )

        viewModel.isLikeClick.observe(viewLifecycleOwner , Observer {
            showPostLikeImageView.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        })

        return fragmentBinding.root
    }


    //스크롤 뷰 높이에 따라 바뀌는 view의 높이를 고정시켜주는 함수
   fun fixOtherViewHeight(){
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
}