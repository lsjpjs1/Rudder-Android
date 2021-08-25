package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityBottomSheetBinding
import com.rudder.databinding.FragmentMyPageBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class CommunityBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var communityBottomSheetBinding : FragmentCommunityBottomSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_community_bottom_sheet, container, false)
    }

//    {
//        communityBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_bottom_sheet, container,false)
//        communityBottomSheetBinding.mainVM = viewModel
//        communityBottomSheetBinding.lifecycleOwner = this
//
//
//        return communityBottomSheetBinding.root
//    }



}