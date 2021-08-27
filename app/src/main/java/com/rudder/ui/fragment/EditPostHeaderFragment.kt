package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.databinding.FragmentEditPostHeaderBinding
import com.rudder.databinding.FragmentShowPostHeaderBinding
import com.rudder.viewModel.MainViewModel

class EditPostHeaderFragment : Fragment() {
    private val viewModel :MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val header = DataBindingUtil.inflate<FragmentEditPostHeaderBinding>(inflater,
            R.layout.fragment_edit_post_header,container,false)
        header.mainVM = viewModel
        return header.root
    }
}