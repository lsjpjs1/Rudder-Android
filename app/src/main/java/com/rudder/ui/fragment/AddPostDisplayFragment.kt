package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rudder.R
import com.rudder.databinding.FragmentAddPostDisplayBinding
import com.rudder.viewModel.MainViewModel

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
        val spinnerAdapter = object : ArrayAdapter<String>(lazyContext,R.layout.support_simple_spinner_dropdown_item,viewModel.categoryNames.value!!){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                return view
            }
        }

        display.mainVM=viewModel
        display.categorySpinner.adapter=spinnerAdapter

        display.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setSelectedCategoryNameInAddPost(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewModel.clearAddPost()
        return display.root
    }
}