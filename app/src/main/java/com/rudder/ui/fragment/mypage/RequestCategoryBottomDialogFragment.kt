package com.rudder.ui.fragment.mypage


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentRequestCategoryBottomDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.EditNicknameDialogViewModel
import com.rudder.viewModel.EditProfileImageDialogViewModel
import com.rudder.viewModel.RequestCategoryViewModel
import kotlinx.android.synthetic.main.fragment_request_category_bottom_dialog.view.*


class RequestCategoryBottomDialogFragment() : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentRequestCategoryBottomDialogBinding
    private val parentActivity by lazy {
        activity as MainActivity
    }

    private lateinit var requestCategoryViewModel: RequestCategoryViewModel


    override fun getTheme(): Int = R.style.DialogStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_category_bottom_dialog, container,false)
        requestCategoryViewModel = ViewModelProvider(this).get(RequestCategoryViewModel::class.java)

        binding.myPageVM = requestCategoryViewModel
        binding.lifecycleOwner = this

        val toastStringBlank: Toast = Toast.makeText(
            parentActivity,
            "One or more fields is blank!",
            Toast.LENGTH_LONG
        )

        val toastRequestCategorySuccess: Toast = Toast.makeText(
            parentActivity,
            "Successfully Submitted.",
            Toast.LENGTH_LONG
        )

        val toastRequestCategoryFail: Toast = Toast.makeText(
            parentActivity,
            "Failed.",
            Toast.LENGTH_LONG
        )


        binding.root.requestButton.setOnClickListener {
            requestCategoryViewModel.requestCategoryName()

        }

        requestCategoryViewModel.isStringBlank.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastStringBlank.show()
                }
            }
        })


        requestCategoryViewModel.isRequestCategorySuccess.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastRequestCategorySuccess.show()
                    parentActivity.requestCategoryBottomDialogFragment.dismiss()
                }
                else {
                    toastRequestCategoryFail.show()
                }
            }
        })


        return binding.root
    }



}