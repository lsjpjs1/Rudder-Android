package com.rudder.ui.fragment.mypage

import android.content.DialogInterface
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentEditNicknameDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.EditNicknameDialogViewModel

class EditNicknameDialogFragment() : DialogFragment() {
    private val viewModel: EditNicknameDialogViewModel by activityViewModels()
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
        val binding = DataBindingUtil.inflate<FragmentEditNicknameDialogBinding>(inflater, R.layout.fragment_edit_nickname_dialog, container,false)
        binding.editNicknameDialogVM = viewModel
        binding.editNicknameDialogFragment = this
        binding.lifecycleOwner = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val displayDpValue = parentActivity.getDisplaySize() // [0] == width, [1] == height

        var lp = binding.editNicknameDialogDisplayConstraintLayout.layoutParams
        lp.height = (displayDpValue[1] * 0.35).toInt()
        lp.width = (displayDpValue[0] * 0.9).toInt()
        binding.editNicknameDialogConstraintLayout.layoutParams = lp


        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let{
                Toast.makeText(parentActivity,it,Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.closeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                if(it){
                    close()
                }
            }
        })

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.clear()

        super.onDismiss(dialog)
    }
    fun close(){
        dismiss()
    }
}