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
import com.rudder.databinding.FragmentCommentEditSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel


class CommunityCommentEditFragment(val viewModel: MainViewModel) : DialogFragment() {


    private lateinit var commentEditSheetFragmentBinding : FragmentCommentEditSheetBinding

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
        commentEditSheetFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_edit_sheet, container,false)
        commentEditSheetFragmentBinding.mainVM = viewModel
        commentEditSheetFragmentBinding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)


        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height

        var lp1 = commentEditSheetFragmentBinding.constraintLayout1.layoutParams
        lp1.height = (displayDpValue[1] * 0.4).toInt()
        lp1.width = (displayDpValue[0] * 0.9).toInt()
        commentEditSheetFragmentBinding.constraintLayout1.layoutParams = lp1

        viewModel.isEditCommentSuccess.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    parentActivity.communityCommentEditFragment.dismiss()
                    parentActivity.communityCommentBottomSheetFragment.dismiss()
                }
            }
        })

        viewModel.isCommentEditDialogCancel.observe(this, Observer {
                event ->
            event.getContentIfNotHandled()?.let {
                if(it){
                    if (parentActivity.communityCommentEditFragment.isAdded){
                        parentActivity.communityCommentEditFragment.dismiss()
                    }
                }
            }

        })
        return commentEditSheetFragmentBinding.root
    }



}