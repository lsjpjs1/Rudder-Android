package com.rudder.ui.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel

class EditPostFragment() : Fragment() {
    private val parentActivity : MainActivity by lazy { activity as MainActivity }
    lateinit var addPostContentsFragment: AddPostContentsFragment

    companion object{
        const val TAG = "EditPostFragment"
    }

    private val viewModel : MainViewModel by activityViewModels()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,R.layout.fragment_add_post_display,container,false)
        addPostContentsFragment = AddPostContentsFragment(viewModel,true)

        childFragmentManager.beginTransaction()
            .replace(R.id.addPostHeaderFrameLayout, EditPostHeaderFragment(viewModel))
            .add(R.id.addPostContentsFrameLayout, addPostContentsFragment)
            .commit()

        fragmentBinding.mainVM=viewModel
        fragmentBinding.lifecycleOwner = this


        viewModel.isEditPostSuccess.observe(viewLifecycleOwner, Observer {
//            fragmentBinding.findNavController().popBackStack()
//            (activity as MainActivity).mainBottomNavigationAppear()

            parentActivity.findNavController(R.id.mainDisplayContainerView).popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()

        })

        return fragmentBinding.root
    }
}