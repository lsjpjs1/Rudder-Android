package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentShowPostBinding
import com.rudder.ui.adapter.PostCommentsAdapter
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_show_post.*

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
        val currentPost = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        fragmentBinding.post = currentPost
        fragmentBinding.mainVM = viewModel
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            adapter.updateComments(it)
        })

        viewModel.commentCountChange.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        })

        viewModel.selectedPostPosition.observe(viewLifecycleOwner, Observer {
            fragmentBinding.post = viewModel.posts.value!![viewModel.selectedPostPosition.value!!]
        })
        viewModel.isLikePost.observe(viewLifecycleOwner, Observer {
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

        fragmentBinding.postMoreImageView.setOnClickListener (postMoreOnclickListener)


        return fragmentBinding.root
    }

    val postMoreOnclickListener = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            val popupMenu = PopupMenu(lazyContext,view)
            MenuInflater(lazyContext).inflate(R.menu.post_pop_up_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    if (p0 != null) {
                        when (p0.itemId){
                            R.id.post_edit->Log.d("edit","edit")
                            R.id.post_delete->Log.d("delete","delete")
                        }

                    }else{
                        return false
                    }
                    return false
                }
            })
            popupMenu.show()
        }

    }

    override fun onPause() {
        viewModel.clearNestedCommentInfo()
        super.onPause()
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