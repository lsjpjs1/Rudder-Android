package com.rudder.ui.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentMainBottomBarBinding
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class MainBottomBarFragment: Fragment() {
    private val viewModel = MainViewModel
    private lateinit var bottomBar: FragmentMainBottomBarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomBar=DataBindingUtil.inflate<FragmentMainBottomBarBinding>(inflater,R.layout.fragment_main_bottom_bar,container,false)
        bottomBar.mainVM = viewModel
        return bottomBar.root
    }

}