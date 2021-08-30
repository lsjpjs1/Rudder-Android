package com.rudder.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentCommentEditSheetBinding
import com.rudder.viewModel.MainViewModel


class CommunityCommentEditFragment : DialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var commentEditSheetFragmentBinding : FragmentCommentEditSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commentEditSheetFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_edit_sheet, container,false)
        commentEditSheetFragmentBinding.mainVM = viewModel
        commentEditSheetFragmentBinding.lifecycleOwner = this


        return commentEditSheetFragmentBinding.root
    }



}