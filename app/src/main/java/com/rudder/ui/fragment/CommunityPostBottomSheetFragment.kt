package com.rudder.ui.fragment


import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityPostBottomSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_community_post_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_community_post_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_school_select.*


class CommunityPostBottomSheetFragment(var viewModel: MainViewModel) : BottomSheetDialogFragment() {

    private lateinit var communityPostBottomSheetBinding : FragmentCommunityPostBottomSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        communityPostBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_post_bottom_sheet, container,false)
        communityPostBottomSheetBinding.mainVM = viewModel
        communityPostBottomSheetBinding.lifecycleOwner = this

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height

        viewModel.selectedPostMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityPostBottomSheetBinding.position = it
                Log.d("selectedPostMorePo","${communityPostBottomSheetBinding.position}")
            }
        })

        viewModel.isPostMine.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(!it) { // ismine == false, 내께 아니면
                    postBottomSheetCL2.visibility = View.GONE
                    postBottomSheetCL3.visibility = View.GONE

                    var lp4 = communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams
                    lp4.height = (displayDpValue[1] * 0.05).toInt()
                    communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp4

                    var lp1 = communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams
                    lp1.height = (displayDpValue[1] * 0.08).toInt()
                    communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams = lp1
                }
        }})

        viewModel.isPostReport.observe(viewLifecycleOwner, Observer {
            event->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    if (!parentActivity.communityPostReportFragment.isAdded)
                        parentActivity.communityPostReportFragment = CommunityPostReportFragment(viewModel)
                    parentActivity.communityPostReportFragment.show(
                        parentActivity.supportFragmentManager,
                        parentActivity.communityPostReportFragment.tag
                    )
                }
            }

        })

        viewModel.isPostEdit.observe(viewLifecycleOwner, Observer {
            it?.let {
                parentActivity.communityPostBottomSheetFragment.dismiss()
                val fragmentShowHide = FragmentShowHide(parentActivity.supportFragmentManager)
                fragmentShowHide.addToBackStack()
                fragmentShowHide.hideFragment(parentActivity.mainBottomBarFragment)

                if (parentActivity.addCommentFragment.isAdded) {
                    fragmentShowHide.hideFragment(parentActivity.addCommentFragment)
                }
                parentActivity.editPostFragment = EditPostFragment(viewModel)
                fragmentShowHide.addFragment(parentActivity.editPostFragment, R.id.mainDisplay, "editPost")
                fragmentShowHide.showFragment(parentActivity.editPostFragment, R.id.mainDisplay)
            }

        })


        var lp1 = communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams
        lp1.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams = lp1

        var lp2 = communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams
        lp2.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams = lp2

        var lp3 = communityPostBottomSheetBinding.postBottomSheetCL3.layoutParams
        lp3.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL3.layoutParams = lp3

        var lp4 = communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams
        lp4.height = (displayDpValue[1] * 0.05).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp4

        return communityPostBottomSheetBinding.root
    }


    override fun onDismiss(dialog: DialogInterface) {
        viewModel.setIsPostEdit(null)
        super.onDismiss(dialog)
    }
}