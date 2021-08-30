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
import com.rudder.databinding.FragmentCommunityPostBottomSheetBinding
import com.rudder.viewModel.MainViewModel


class CommunityPostBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var communityPostBottomSheetBinding : FragmentCommunityPostBottomSheetBinding

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

        communityPostBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_post_bottom_sheet, container,false)
        communityPostBottomSheetBinding.mainVM = viewModel
        communityPostBottomSheetBinding.lifecycleOwner = this

        viewModel.selectedPostMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityPostBottomSheetBinding.position = it

                Log.d("selectedPostMorePo","${communityPostBottomSheetBinding.position}")

            }
        })

        return communityPostBottomSheetBinding.root
    }


}