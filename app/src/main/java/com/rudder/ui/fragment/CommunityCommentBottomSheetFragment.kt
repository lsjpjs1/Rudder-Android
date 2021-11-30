package com.rudder.ui.fragment


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentBottomSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_community_comment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityCommentBottomSheetFragment(val viewModel: MainViewModel) : BottomSheetDialogFragment() {


    private lateinit var communityCommentBottomSheetBinding : FragmentCommunityCommentBottomSheetBinding

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
        communityCommentBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_comment_bottom_sheet, container,false)
        communityCommentBottomSheetBinding.mainVM = viewModel
        communityCommentBottomSheetBinding.lifecycleOwner = this

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height


        viewModel.selectedCommentMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityCommentBottomSheetBinding.position = it
                Log.d("comttomSheetBinding","${communityCommentBottomSheetBinding.position}")
            }
        })

        viewModel.isCommentMine.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(!it) { // ismine == false, 내께 아니면
                    commentBottomSheetCL2.visibility = View.GONE
                    commentBottomSheetCL3.visibility = View.GONE

                    var lp4 = communityCommentBottomSheetBinding.commentBottomSheetCL4.layoutParams
                    lp4.height = (displayDpValue[1] * 0.05).toInt()
                    communityCommentBottomSheetBinding.commentBottomSheetCL4.layoutParams = lp4

                    var lp1 = communityCommentBottomSheetBinding.commentBottomSheetCL1.layoutParams
                    lp1.height = (displayDpValue[1] * 0.08).toInt()
                    communityCommentBottomSheetBinding.commentBottomSheetCL1.layoutParams = lp1
                }
            }})

        viewModel.isCommentDelete.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    (activity as MainActivity).closeCommunityBottomSheetFragment()
                }
            }
        })

        viewModel.isCommentReport.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                if (!parentActivity.communityCommentReportFragment.isAdded)
                    parentActivity.communityCommentReportFragment = CommunityCommentReportFragment(viewModel)
                    parentActivity.communityCommentReportFragment.show(
                        parentActivity.supportFragmentManager,
                        parentActivity.communityCommentReportFragment.tag
                    )
            }
        })

        viewModel.isCommentEdit.observe(this, Observer {
            it.getContentIfNotHandled().let{
                if (!parentActivity.communityCommentEditFragment.isAdded)
                    parentActivity.communityCommentEditFragment = CommunityCommentEditFragment(viewModel)
                    parentActivity.communityCommentEditFragment.show(
                        parentActivity.supportFragmentManager,
                        parentActivity.communityCommentEditFragment.tag
                    )
            }
        })

        var lp1 = communityCommentBottomSheetBinding.commentBottomSheetCL1.layoutParams
        lp1.height = (displayDpValue[1] * 0.08).toInt()
        communityCommentBottomSheetBinding.commentBottomSheetCL1.layoutParams = lp1

        var lp2 = communityCommentBottomSheetBinding.commentBottomSheetCL2.layoutParams
        lp2.height = (displayDpValue[1] * 0.08).toInt()
        communityCommentBottomSheetBinding.commentBottomSheetCL2.layoutParams = lp2

        var lp3 = communityCommentBottomSheetBinding.commentBottomSheetCL3.layoutParams
        lp3.height = (displayDpValue[1] * 0.08).toInt()
        communityCommentBottomSheetBinding.commentBottomSheetCL3.layoutParams = lp3

        var lp4 = communityCommentBottomSheetBinding.commentBottomSheetCL4.layoutParams
        lp4.height = (displayDpValue[1] * 0.05).toInt()
        communityCommentBottomSheetBinding.commentBottomSheetCL4.layoutParams = lp4

        return communityCommentBottomSheetBinding.root
    }



}