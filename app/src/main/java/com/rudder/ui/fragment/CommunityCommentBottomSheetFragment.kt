package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityCommentBottomSheetBinding
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityCommentBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var communityCommentBottomSheetBinding : FragmentCommunityCommentBottomSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        super.onCreateView(inflater, container, savedInstanceState)
//        return inflater.inflate(R.layout.fragment_community_bottom_sheet, container, false)

        communityCommentBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_comment_bottom_sheet, container,false)
        communityCommentBottomSheetBinding.mainVM = viewModel
        communityCommentBottomSheetBinding.lifecycleOwner = this

        viewModel.selectedCommentMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityCommentBottomSheetBinding.position = it
                Log.d("comttomSheetBinding","${communityCommentBottomSheetBinding.position}")

            }
        })

        return communityCommentBottomSheetBinding.root
    }


}