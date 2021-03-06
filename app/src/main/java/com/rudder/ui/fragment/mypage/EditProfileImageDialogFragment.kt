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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.data.DisplaySize
import com.rudder.databinding.FragmentEditProfileImageDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.EditProfileImagesAdapter
import com.rudder.ui.fragment.MyPageFragmentInterface
import com.rudder.viewModel.EditNicknameDialogViewModel
import com.rudder.viewModel.EditProfileImageDialogViewModel

class EditProfileImageDialogFragment(val myPageFragmentInterface: MyPageFragmentInterface) : DialogFragment() {

    //val editProfileImageDialogViewModel: EditProfileImageDialogViewModel by activityViewModels()
    lateinit var editProfileImageDialogViewModel: EditProfileImageDialogViewModel


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
        val binding = DataBindingUtil.inflate<FragmentEditProfileImageDialogBinding>(
            inflater,
            R.layout.fragment_edit_profile_image_dialog,
            container,
            false
        )

        editProfileImageDialogViewModel = ViewModelProvider(this).get(EditProfileImageDialogViewModel::class.java)

        binding.editProfileImageVM = editProfileImageDialogViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val displayDpValue = parentActivity.getDisplaySize() // [0] == width, [1] == height

        var lp = binding.editProfileImageDialogDisplayConstraintLayout.layoutParams
        lp.height = (displayDpValue[1] * 0.35).toInt()
        lp.width = (displayDpValue[0] * 0.9).toInt()
        binding.editProfileImageDialogDisplayConstraintLayout.layoutParams = lp


        //????????? ????????? ?????????????????? ??????
        val displaySize = DisplaySize(parentActivity.getDisplaySize()[0],parentActivity.getDisplaySize()[1])
        val adapter = EditProfileImagesAdapter(displaySize,this)
        adapter.submitList(
            arrayListOf(
            )
        )

        binding.editProfileImageRecyclerView.also {
            it.layoutManager =
                LinearLayoutManager(lazyContext, LinearLayoutManager.HORIZONTAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        editProfileImageDialogViewModel._profileImageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it.toMutableList().map { it.copy() })
            }
        })

        editProfileImageDialogViewModel._toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let{
                Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
            }
        })

        editProfileImageDialogViewModel._closeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it){
                    dismiss()
                }
            }
        })

        editProfileImageDialogViewModel._myProfileImageUrl.observe(requireParentFragment().viewLifecycleOwner, Observer {
            it?.let{
                myPageFragmentInterface.setMyProfileImageUrl(it)
            }
        })

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        //editProfileImageDialogViewModel.clear()
        super.onDismiss(dialog)
    }

}