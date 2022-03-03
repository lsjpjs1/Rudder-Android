package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.postmessage.PostMessageRoomFragmentArgs
import com.rudder.ui.fragment.search.SearchPostDisplayFragmentArgs
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.SearchViewModel

class EditPostFragment() : Fragment() {
    private val parentActivity : MainActivity by lazy { activity as MainActivity }
    lateinit var addPostContentsFragment: AddPostContentsFragment

    companion object{
        const val TAG = "EditPostFragment"
        const val MAIN_VIEW_MODEL = 1
        const val SEARCH_VIEW_MODEL = 2
        const val NOTIFICATION_VIEW_MODEL = 3
        const val MY_POST_VIEW_MODEL = 4
        const val MY_COMMENT_VIEW_MODEL = 5


    }

    lateinit var viewModel : MainViewModel
    private val args : SearchPostDisplayFragmentArgs by navArgs()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val toastEditSuccess = Toast.makeText(activity, "Successfully edit post!", Toast.LENGTH_LONG)

        when (args.viewModelIndex) {
            ShowPostDisplayFragment.SEARCH_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SearchViewModel::class.java)
            }
            ShowPostDisplayFragment.MAIN_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MainViewModel::class.java)
            }
            ShowPostDisplayFragment.NOTIFICATION_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(NotificationViewModel::class.java)
            }
            ShowPostDisplayFragment.MY_POST_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyPostViewModel::class.java)
            }
            ShowPostDisplayFragment.MY_COMMENT_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyCommentViewModel::class.java)
            }
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
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    val navController = parentActivity.findNavController(R.id.mainDisplayContainerView)
                    navController.popBackStack()
                    toastEditSuccess.show()

                    if (navController.currentDestination!!.label == "SearchPostDisplayFragment") {
                        (activity as MainActivity).nestedCommentDisappear()
                    } else if (navController.currentDestination!!.label == "MyPostDisplayFragment") {
                        (activity as MainActivity).nestedCommentDisappear()
                    } else if (navController.currentDestination!!.label == "ShowPostDisplayFragment") {
                        (activity as MainActivity).nestedCommentDisappear()
                    }
                    else {
                        (activity as MainActivity).mainBottomNavigationAppear()
                        (activity as MainActivity).nestedCommentDisappear()
                    }
                }
            }

        })

        return fragmentBinding.root
    }
}