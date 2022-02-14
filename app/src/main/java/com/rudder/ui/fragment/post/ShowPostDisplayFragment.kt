package com.rudder.ui.fragment.post

import android.os.Bundle
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
import com.rudder.viewModel.SearchViewModel


class ShowPostDisplayFragment : Fragment() {


    companion object{
        const val TAG = "ShowPostDisplayFragment"
        const val SEARCH_VIEW_MODEL = 2
        const val MAIN_VIEW_MODEL = 1
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


        if (args.viewModelIndex == SEARCH_VIEW_MODEL) {
            viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(SearchViewModel::class.java)
        } else {
            viewModel = ViewModelProvider(activity as ViewModelStoreOwner).get(MainViewModel::class.java)
        }

        val showPostDisplayBinding= DataBindingUtil.inflate<FragmentShowPostDisplayBinding>(inflater,
            R.layout.fragment_show_post_display,container,false)

        showPostDisplayBinding.lifecycleOwner = this







        return showPostDisplayBinding.root

    }


}