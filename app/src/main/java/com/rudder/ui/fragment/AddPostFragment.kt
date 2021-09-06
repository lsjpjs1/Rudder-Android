package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentAddPostBinding
import com.rudder.databinding.FragmentShowPostBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_school_select.*

class AddPostFragment : Fragment() {
    private val viewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentAddPostBinding>(inflater,R.layout.fragment_add_post,container,false)
        childFragmentManager.beginTransaction()
            .add(R.id.addPostHeader,AddPostHeaderFragment())
            .add(R.id.addPostDisplay,AddPostDisplayFragment())
            .commit()

        fragmentBinding.mainVM = viewModel
        fragmentBinding.lifecycleOwner = this





        return fragmentBinding.root
    }
}