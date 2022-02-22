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
import com.rudder.R
import com.rudder.databinding.FragmentAlertDialogBinding
import com.rudder.databinding.FragmentEditNicknameDialogBinding
import com.rudder.databinding.FragmentSendPostMessageDialogBinding
import com.rudder.util.AlertDialogListener

class AlertDialogFragment private constructor(val alertDialogListener: AlertDialogListener, val text: String): DialogFragment(){

    companion object{
        const val TAG = "AlertDialogFragment"
        fun instance(alertDialogListener: AlertDialogListener, text: String): AlertDialogFragment {
            return AlertDialogFragment(alertDialogListener, text)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAlertDialogBinding>(inflater, R.layout.fragment_alert_dialog, container,false)
        binding.lifecycleOwner = this

        binding.alertDialogTV.text = text
        binding.alertDialogOkButton.setOnClickListener {
            alertDialogListener.onOkClick()
        }
        binding.alertDialogCancelButton.setOnClickListener {
            alertDialogListener.onCancelClick()
            dismiss()
        }

        return binding.root
    }

}