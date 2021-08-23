package com.rudder.ui.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentMyPageBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment: Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var myPageBinding : FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myPageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page,container,false)
        myPageBinding.mainVM = viewModel
        myPageBinding.lifecycleOwner = this

        childFragmentManager.beginTransaction()
            .add(R.id.myPageHeader, MyPageHeaderFragment())
            .commit()

        return myPageBinding.root
    }
}