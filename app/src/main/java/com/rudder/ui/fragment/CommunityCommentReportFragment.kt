package com.rudder.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentReportSheetBinding
import com.rudder.databinding.FragmentCommunityPostReportSheetBinding
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityCommentReportFragment : DialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var communityCommentReportFragmentBinding : FragmentCommunityCommentReportSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        communityCommentReportFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_comment_report_sheet, container,false)
        communityCommentReportFragmentBinding.mainVM = viewModel
        communityCommentReportFragmentBinding.lifecycleOwner = this


        return communityCommentReportFragmentBinding.root
    }



}