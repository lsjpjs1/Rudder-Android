package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class SchoolSelectFragment : Fragment() {

    //private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }

    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var schoolSelectBinding : FragmentSchoolSelectBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        schoolSelectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_school_select,container,false)
        schoolSelectBinding.signUpVM = viewModel
        schoolSelectBinding.lifecycleOwner = this

        val toastSchoolSelect = Toast.makeText(activity, "Please Select, Your School", Toast.LENGTH_SHORT)

        viewModel.schoolSelectFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(schoolSelectCheckbox)
                    toastSchoolSelect.cancel()
                }
                else {
                    ChangeUIState.changeCheckBoxFalseState(schoolSelectCheckbox)
                    toastSchoolSelect.show()
                }
                ChangeUIState.buttonEnable(schoolSelectNextBtn,schoolSelectCheckbox.isChecked)
        }})

        var adapter = ArrayAdapter<String>(lazyContext,R.layout.support_simple_spinner_dropdown_item,viewModel.schoolList.value!!)
        schoolSelectBinding.schoolSelectSpinner.adapter = adapter

        schoolSelectBinding.root.schoolSelectCheckbox.isEnabled = false
        schoolSelectBinding.root.schoolSelectNextBtn.isEnabled = false

        return schoolSelectBinding.root
    }
}