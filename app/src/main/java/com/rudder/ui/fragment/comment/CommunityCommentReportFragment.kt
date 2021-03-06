package com.rudder.ui.fragment.comment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentReportSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.AlertDialogFragment
import com.rudder.util.AlertDialogListener
import com.rudder.viewModel.MainViewModel


class CommunityCommentReportFragment(val mainViewModel: MainViewModel) : DialogFragment() {

    private lateinit var communityCommentReportFragmentBinding : FragmentCommunityCommentReportSheetBinding
    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        communityCommentReportFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_comment_report_sheet, container,false)
        communityCommentReportFragmentBinding.mainVM = mainViewModel
        communityCommentReportFragmentBinding.communityCommentReportFragment = this
        communityCommentReportFragmentBinding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        var lp1 = communityCommentReportFragmentBinding.constraintLayout1.layoutParams
        lp1.height = (displayDpValue[1] * 0.4).toInt()
        lp1.width = (displayDpValue[0] * 0.9).toInt()
        communityCommentReportFragmentBinding.constraintLayout1.layoutParams = lp1

        mainViewModel.isCommentReportDialogCancel.observe(this, Observer {
                event ->
            event.getContentIfNotHandled()?.let {
                if(it){
                    if (parentActivity.communityCommentReportFragment.isAdded){
                        parentActivity.communityCommentReportFragment.dismiss()
                    }
                }
            }
        })

        mainViewModel.isReportCommentSuccess.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    parentActivity.communityCommentReportFragment.dismiss()
                    parentActivity.communityCommentBottomSheetFragment.dismiss()
                }
            }
        })


        return communityCommentReportFragmentBinding.root
    }

    fun showCommentReportAlert() {
        val alertDialogFragment = AlertDialogFragment.instance(
            object : AlertDialogListener {
                override fun onOkClick() {
                    mainViewModel.reportComment()
                }

                override fun onCancelClick() {

                }
            },
            "Do you want to Report this comment?"
        )
        alertDialogFragment.show(childFragmentManager, AlertDialogFragment.TAG)
    }


}