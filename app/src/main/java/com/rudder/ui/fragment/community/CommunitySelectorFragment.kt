package com.rudder.ui.fragment.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentCommuintySelectorBinding
import com.rudder.ui.adapter.CategorySelectorAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.category_selector.view.*

class CommunitySelectorFragment : Fragment(),CustomOnclickListener {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    private lateinit var adapter : CategorySelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding= DataBindingUtil.inflate<FragmentCommuintySelectorBinding>(inflater,R.layout.fragment_commuinty_selector,container,false)
        adapter = CategorySelectorAdapter(mainViewModel.userSelectCategories.value!!,mainViewModel.selectedCategoryPosition.value!!,lazyContext,this)
        fragmentBinding.categoryRecyclerView.also {
            it.layoutManager = LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        mainViewModel.selectedCategoryView.observe(viewLifecycleOwner, Observer {
            it?.also {
                it.categoryUnderBarView.visibility = View.VISIBLE
                it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.black))
            }
        })


        mainViewModel.userSelectCategories.observe(viewLifecycleOwner, Observer {
            adapter.updateCategories(it)
        })


        return fragmentBinding.root
    }

    override fun onClickView(view: View, position: Int) {
        mainViewModel.selectedCategoryView.value?.also {
            it.categoryUnderBarView.visibility = View.GONE
            it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.grey))
        }
        mainViewModel.setSelectedCategoryPosition(position)
        mainViewModel.setSelectedCategoryView(view)
        mainViewModel.clearPosts()
        //mainViewModel.getPosts()
        mainViewModel.getPostsFun()

    }

}