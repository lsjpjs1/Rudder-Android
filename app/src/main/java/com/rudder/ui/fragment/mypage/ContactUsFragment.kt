package com.rudder.ui.fragment.mypage

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
import com.rudder.R
import com.rudder.databinding.FragmentContactUsDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class ContactUsFragment : DialogFragment() {

    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var contectUsFragmentBinding : FragmentContactUsDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contectUsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us_dialog, container,false)
        contectUsFragmentBinding.mainVM = mainViewModel
        contectUsFragmentBinding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height

        var lp1 = contectUsFragmentBinding.constraintLayout1.layoutParams
        lp1.height = (displayDpValue[1] * 0.4).toInt()
        lp1.width = (displayDpValue[0] * 0.9).toInt()
        contectUsFragmentBinding.constraintLayout1.layoutParams = lp1

        return contectUsFragmentBinding.root
    }



}