package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.FragmentAddCommentBinding
import com.rudder.databinding.FragmentMainBottomBarBinding
import com.rudder.viewModel.AddCommentViewModel
import com.rudder.viewModel.MainViewModel

class AddCommentFragment : Fragment() {
    private val viewModel : AddCommentViewModel by lazy {
        ViewModelProvider(this).get(AddCommentViewModel::class.java)
    }
    private lateinit var AddComment: FragmentAddCommentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AddComment= DataBindingUtil.inflate<FragmentAddCommentBinding>(inflater,
            R.layout.fragment_add_comment,container,false)
        AddComment.addCommentVM = viewModel
        return AddComment.root
    }


}