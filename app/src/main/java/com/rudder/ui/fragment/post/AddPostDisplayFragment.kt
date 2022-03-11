package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.viewModel.MainViewModel

class AddPostDisplayFragment : Fragment() {
    private val viewModel : MainViewModel by activityViewModels()

    companion object{
        const val TAG = "AddPostDisplayFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding= DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,R.layout.fragment_add_post_display,container,false)
        childFragmentManager.beginTransaction()
            .add(R.id.addPostContentsFrameLayout, AddPostContentsFragment(viewModel,false))
            .commit()

        fragmentBinding.mainVM = viewModel
        fragmentBinding.lifecycleOwner = this

        return fragmentBinding.root
    }
}