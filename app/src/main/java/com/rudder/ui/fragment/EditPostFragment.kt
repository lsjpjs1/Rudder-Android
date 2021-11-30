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
import com.rudder.ui.activity.MainActivity
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_post_display.*
import kotlinx.android.synthetic.main.fragment_add_post_display.view.*

class EditPostFragment(val viewModel: MainViewModel) : Fragment() {
    private val parentActivity : MainActivity by lazy { activity as MainActivity }
    lateinit var addPostDisplayFragment: AddPostDisplayFragment
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentAddPostBinding>(inflater,R.layout.fragment_add_post,container,false)
        addPostDisplayFragment = AddPostDisplayFragment(viewModel,true)

        childFragmentManager.beginTransaction()
            .add(R.id.addPostHeader, EditPostHeaderFragment(viewModel))
            .add(R.id.addPostDisplay, addPostDisplayFragment)
            .commit()

        fragmentBinding.mainVM=viewModel
        fragmentBinding.lifecycleOwner = this





        viewModel.isEditPostSuccess.observe(viewLifecycleOwner, Observer {
            parentActivity.onBackPressed()
        })

        return fragmentBinding.root
    }
}