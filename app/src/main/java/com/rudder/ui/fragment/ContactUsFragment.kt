package com.rudder.ui.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentReportSheetBinding
import com.rudder.databinding.FragmentContactUsDialogBinding
import com.rudder.viewModel.MainViewModel

class ContactUsFragment : DialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var contectUsFragmentBinding : FragmentContactUsDialogBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contectUsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us_dialog, container,false)
        contectUsFragmentBinding.mainVM = viewModel
        contectUsFragmentBinding.lifecycleOwner = this


        return contectUsFragmentBinding.root
    }



}