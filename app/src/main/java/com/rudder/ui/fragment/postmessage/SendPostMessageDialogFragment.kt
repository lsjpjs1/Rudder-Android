package com.rudder.ui.fragment.postmessage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentSendPostMessageDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.SendPostMessageDialogViewModel

class SendPostMessageDialogFragment(val receiveUserInfoId: Int) : DialogFragment() {

    private val viewModel: SendPostMessageDialogViewModel by viewModels()

    private val parentActivity by lazy {
        activity as MainActivity
    }
    private val lazyContext by lazy {
        context
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSendPostMessageDialogBinding>(inflater, R.layout.fragment_send_post_message_dialog, container,false)

        binding.lifecycleOwner = this
        binding.sendPostMessageDialogVM = viewModel
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        viewModel.setMessageReceiveUserInfoId(receiveUserInfoId)

        val displayDpValue = parentActivity.getDisplaySize() // [0] == width, [1] == height

        var lp = binding.sendPostMessageDialogCL.layoutParams
        lp.height = (displayDpValue[1] * 0.45).toInt()
        lp.width = (displayDpValue[0] * 0.9).toInt()
        binding.sendPostMessageDialogCL.layoutParams = lp


        viewModel.closeFlag.observe(viewLifecycleOwner, Observer {
            it?.let {
                close()
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(lazyContext, it, Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }


    fun close(){
        dismiss()
    }
}