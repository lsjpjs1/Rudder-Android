package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_post_display.*

class AddPostDisplayFragment : Fragment() {
    private val viewModel : MainViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val display = DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,
                R.layout.fragment_add_post_display,container,false)
        display.mainVM=viewModel
        viewModel.clearAddPostBody()
        return display.root
    }
}