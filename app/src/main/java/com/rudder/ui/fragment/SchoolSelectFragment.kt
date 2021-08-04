package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.ui.activity.SignUpActivity
import com.rudder.util.ChangeUIState
import com.rudder.util.FragmentShowHide
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*


class SchoolSelectFragment : Fragment() {

    private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }
    private lateinit var schoolSelect : FragmentSchoolSelectBinding

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        schoolSelect = DataBindingUtil.inflate<FragmentSchoolSelectBinding>(inflater,R.layout.fragment_school_select,container,false)
        schoolSelect.signUpVM = viewModel
        schoolSelect.lifecycleOwner = this

        val toastSchoolSelect = Toast.makeText(activity, "Please Select, Your School", Toast.LENGTH_SHORT)

        viewModel.schoolSelectFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(schoolSelectCheckbox)
                    toastSchoolSelect.cancel()
                    ChangeUIState.buttonEnable(schoolSelectNextBtn,schoolSelectCheckbox.isChecked) }
                else{
                    ChangeUIState.changeCheckBoxFalseState(schoolSelectCheckbox)
                    toastSchoolSelect.show()
                    ChangeUIState.buttonEnable(schoolSelectNextBtn,schoolSelectCheckbox.isChecked)}
        }})

        schoolSelect.schoolSelectSpinner.adapter = ArrayAdapter.createFromResource(lazyContext, R.array.schools_array , R.layout.support_simple_spinner_dropdown_item)
            .also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                schoolSelect.schoolSelectSpinner.adapter = adapter
            }


        schoolSelect.root.schoolSelectCheckbox.isEnabled = false
        schoolSelect.root.schoolSelectNextBtn.isEnabled = false

        return schoolSelect.root
    }
}