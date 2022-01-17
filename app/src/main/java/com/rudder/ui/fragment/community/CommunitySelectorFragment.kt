package com.rudder.ui.fragment.community

import android.os.Bundle
import android.util.Log
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
    private val viewModel: MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    private lateinit var adapter : CategorySelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding= DataBindingUtil.inflate<FragmentCommuintySelectorBinding>(inflater,R.layout.fragment_commuinty_selector,container,false)
        adapter = CategorySelectorAdapter(viewModel.userSelectCategories.value!!,viewModel.selectedCategoryPosition.value!!,lazyContext,this)
        fragmentBinding.categoryRecyclerView.also {
            it.layoutManager = LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        viewModel.selectedCategoryView.observe(viewLifecycleOwner, Observer {
            it?.also {
                it.categoryUnderBarView.visibility = View.VISIBLE
                it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.black))
            }


        })

        viewModel.userSelectCategories.observe(viewLifecycleOwner, Observer {
            Log.d("error4",it.toString())
            adapter.updateCategories(it)
        })
        return fragmentBinding.root
    }

    override fun onClick(view: View, position: Int) {
        viewModel.selectedCategoryView.value?.also {
            it.categoryUnderBarView.visibility = View.GONE
            it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.grey))
        }
        viewModel.setSelectedCategoryPosition(position)
        viewModel.setSelectedCategoryView(view)
        viewModel.clearPosts()
        viewModel.getPosts()
    }

}