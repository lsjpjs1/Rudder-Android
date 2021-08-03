package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.ui.activity.SignUpActivity
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.SignUpViewModel


class SchoolSelectFragment : Fragment() {

    private val viewModel = SignUpViewModel
    private lateinit var schoolSelect : FragmentSchoolSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        schoolSelect = DataBindingUtil.inflate<FragmentSchoolSelectBinding>(inflater,R.layout.fragment_school_select,container,false)
        schoolSelect.signUpVM = viewModel

//        viewModel.schoolSelectNext.observe(viewLifecycleOwner, Observer {
////            val signUpActivityInstance = context as SignUpActivity
////            signUpActivityInstance.asd()
//
//            //val fragmentShowHide = FragmentShowHide(supportFragmentManager)
//        })

        return schoolSelect.root




    }
}