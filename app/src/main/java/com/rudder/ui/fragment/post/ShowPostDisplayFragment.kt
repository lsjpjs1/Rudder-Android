package com.rudder.ui.fragment.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentShowPostDisplayBinding
import com.rudder.ui.fragment.search.SearchPostDisplayFragmentArgs
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.SearchViewModel


class ShowPostDisplayFragment : Fragment() {


    companion object{
        const val TAG = "ShowPostDisplayFragment"
        const val MAIN_VIEW_MODEL = 1
        const val SEARCH_VIEW_MODEL = 2
        const val NOTIFICATION_VIEW_MODEL = 3
        const val MY_POST_VIEW_MODEL = 4
        const val MY_COMMENT_VIEW_MODEL = 5
    }

    private val lazyContext by lazy {
        requireContext()
    }


    private val args : SearchPostDisplayFragmentArgs by navArgs()

    lateinit var viewModel : MainViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        when (args.viewModelIndex) {
            SEARCH_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SearchViewModel::class.java)
            }
            MAIN_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MainViewModel::class.java)
            }
            NOTIFICATION_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(NotificationViewModel::class.java)
            }
            MY_POST_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyPostViewModel::class.java)
            }
            MY_COMMENT_VIEW_MODEL -> {
                viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MyCommentViewModel::class.java)
            }
        }




        val showPostDisplayBinding= DataBindingUtil.inflate<FragmentShowPostDisplayBinding>(inflater,
            R.layout.fragment_show_post_display,container,false)

        showPostDisplayBinding.lifecycleOwner = this


        return showPostDisplayBinding.root

    }


}