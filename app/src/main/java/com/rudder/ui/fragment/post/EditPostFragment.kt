package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.postmessage.PostMessageRoomFragmentArgs
import com.rudder.ui.fragment.search.SearchPostDisplayFragmentArgs
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SearchViewModel

class EditPostFragment() : Fragment() {
    private val parentActivity : MainActivity by lazy { activity as MainActivity }
    lateinit var addPostContentsFragment: AddPostContentsFragment

    companion object{
        const val TAG = "EditPostFragment"
        const val SEARCH_VIEW_MODEL = 2
        const val MAIN_VIEW_MODEL = 1
    }

    lateinit var viewModel : MainViewModel
    private val args : SearchPostDisplayFragmentArgs by navArgs()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (args.viewModelIndex == SEARCH_VIEW_MODEL){
            viewModel = ViewModelProvider(parentActivity).get(SearchViewModel::class.java)
        }else{
            viewModel = ViewModelProvider(parentActivity).get(MainViewModel::class.java)
        }
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