package com.rudder.ui.fragment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentCommunityPostReportSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityPostReportFragment(val viewModel: MainViewModel) : DialogFragment() {


    private lateinit var communityPostReportFragmentBinding : FragmentCommunityPostReportSheetBinding

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
        communityPostReportFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_post_report_sheet, container,false)
        communityPostReportFragmentBinding.mainVM = viewModel
        communityPostReportFragmentBinding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)


        viewModel.isReportPostSuccess.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    parentActivity.communityPostReportFragment.dismiss()
                    parentActivity.communityPostBottomSheetFragment.dismiss()
                }
            }
        })

        viewModel.isReportDialogCancel.observe(this, Observer {
                event ->
            event.getContentIfNotHandled()?.let {
                if(it){
                    if (parentActivity.communityPostReportFragment.isAdded){
                        parentActivity.communityPostReportFragment.dismiss()
                    }
                }
            }

        })


        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height

        var lp1 = communityPostReportFragmentBinding.constraintLayout1.layoutParams
        lp1.height = (displayDpValue[1] * 0.4).toInt()
        lp1.width = (displayDpValue[0] * 0.9).toInt()
        communityPostReportFragmentBinding.constraintLayout1.layoutParams = lp1

        return communityPostReportFragmentBinding.root
    }



}