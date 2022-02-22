package com.rudder.ui.fragment.comment


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentBottomSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.AlertDialogFragment
import com.rudder.ui.fragment.postmessage.SendPostMessageDialogFragment
import com.rudder.util.AlertDialogListener
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_community_comment_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityCommentBottomSheetFragment(val viewModel: MainViewModel) : BottomSheetDialogFragment() {


    private lateinit var communityCommentBottomSheetBinding : FragmentCommunityCommentBottomSheetBinding

    private lateinit var sendPostMessageDialogFragment: SendPostMessageDialogFragment

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
        communityCommentBottomSheetBinding.communityCommentBottomSheetFragment = this

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height


        viewModel.selectedCommentMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityCommentBottomSheetBinding.position = it
            }
        })

        viewModel.isCommentMine.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(!it) { // ismine == false, 내께 아니면
                    communityCommentBottomSheetBinding.commentBottomSheetCL2.visibility = View.GONE
                    communityCommentBottomSheetBinding.commentBottomSheetCL3.visibility = View.GONE

                }else{
                    //communityCommentBottomSheetBinding.commentBottomSheetCL4.visibility = View.GONE
                    communityCommentBottomSheetBinding.commentBottomSheetCL1.visibility = View.GONE
                    communityCommentBottomSheetBinding.sendPostMessageCommentCL.visibility = View.GONE
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
            it.getContentIfNotHandled()?.let{
                if (!parentActivity.communityCommentEditFragment.isAdded) {
                    parentActivity.communityCommentEditFragment = CommunityCommentEditFragment(viewModel)
                    parentActivity.communityCommentEditFragment.show(parentActivity.supportFragmentManager, parentActivity.communityCommentEditFragment.tag
                    )
                }
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

        var lp5 = communityCommentBottomSheetBinding.sendPostMessageCommentCL.layoutParams
        lp5.height = (displayDpValue[1] * 0.08).toInt()
        communityCommentBottomSheetBinding.sendPostMessageCommentCL.layoutParams = lp5


        return communityCommentBottomSheetBinding.root
    }

    fun showPostMessageDialog() {
        val receiveUserInfoId = viewModel.comments.value!![viewModel.selectedCommentMorePosition.value!!].user_info_id
        sendPostMessageDialogFragment = SendPostMessageDialogFragment(receiveUserInfoId)
        sendPostMessageDialogFragment.show(childFragmentManager, "sendPostMessageDialogFragment")
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.dismissCommentMore()
        super.onDismiss(dialog)
    }

    fun showDeleteCommentAlert(){
        val alertDialogFragment = AlertDialogFragment.instance(
            object : AlertDialogListener {
                override fun onOkClick() {
                    viewModel.clickCommentDelete()
                }

                override fun onCancelClick() {

                }

            },
            "Do you really delete this comment?"
        )

        alertDialogFragment.show(childFragmentManager, AlertDialogFragment.TAG)
    }

}