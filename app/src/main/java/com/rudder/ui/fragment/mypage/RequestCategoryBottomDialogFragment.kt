package com.rudder.ui.fragment.mypage


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentRequestCategoryBottomDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.MyPageViewModel
import kotlinx.android.synthetic.main.fragment_add_comment.view.*
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_request_category_bottom_dialog.view.*


class RequestCategoryBottomDialogFragment(val myPageViewModel: MyPageViewModel) : BottomSheetDialogFragment() {


    private lateinit var binding : FragmentRequestCategoryBottomDialogBinding

    private val parentActivity by lazy {
        activity as MainActivity
    }

    override fun getTheme(): Int = R.style.DialogStyle


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_category_bottom_dialog, container,false)
        binding.myPageVM = myPageViewModel
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
            myPageViewModel.requestCategoryName()

        }


        myPageViewModel.isStringBlank.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastStringBlank.show()
                }
            }
        })


        myPageViewModel.isRequestCategorySuccess.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastRequestCategorySuccess.show()
                    //this.onDismiss(RequestCategoryBottomDialogFragment())
                    //onCancel(dialog)
                }
                else {
                    toastRequestCategoryFail.show()
                }
            }
        })


        return binding.root
    }


}