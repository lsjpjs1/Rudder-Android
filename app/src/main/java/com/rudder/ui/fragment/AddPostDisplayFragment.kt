package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.data.remote.Category
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.databinding.FragmentAddPostHeaderBinding
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_post_display.*

class AddPostDisplayFragment : Fragment() {
    private val viewModel : MainViewModel by activityViewModels()
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val display = DataBindingUtil.inflate<FragmentAddPostDisplayBinding>(inflater,
                R.layout.fragment_add_post_display,container,false)
        val spinnerAdapter = object : ArrayAdapter<Category>(lazyContext,R.layout.support_simple_spinner_dropdown_item){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                return view
            }
        }

        spinnerAdapter.addAll(viewModel.categories.value!!)

        display.mainVM=viewModel
        display.categorySpinner.adapter=spinnerAdapter

        viewModel.clearAddPostBody()
        return display.root
    }
}