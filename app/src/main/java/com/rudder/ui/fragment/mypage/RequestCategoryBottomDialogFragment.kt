package com.rudder.ui.fragment.mypage


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentRequestCategoryBottomDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MyPageViewModel


class RequestCategoryBottomDialogFragment(val viewModel: MyPageViewModel) : BottomSheetDialogFragment() {


    private lateinit var binding : FragmentRequestCategoryBottomDialogBinding

    private val parentActivity by lazy {
        activity as MainActivity
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_category_bottom_dialog, container,false)
        binding.myPageVM = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }


}