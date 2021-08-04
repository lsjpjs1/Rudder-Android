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

    private val viewModel = SignUpViewModel
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


        val toastSchoolSelect = Toast.makeText(activity, "Please Select, Your School", Toast.LENGTH_SHORT)

        viewModel.schoolSelectFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(schoolSelectCheckbox)
                    toastSchoolSelect.cancel() }
                else{
                    ChangeUIState.changeCheckBoxFalseState(schoolSelectCheckbox)
                    toastSchoolSelect.show() }
        }})


        schoolSelect.schoolSelectSpinner.adapter = ArrayAdapter.createFromResource(lazyContext, R.array.schools_array , R.layout.support_simple_spinner_dropdown_item)
            .also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                schoolSelect.schoolSelectSpinner.adapter = adapter
            }
//
//        schoolSelect.schoolSelectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                ///
//
//                //Log.d(ContentValues.TAG, "spinner 결과 : ${p2}")
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }

        schoolSelect.root.schoolSelectCheckbox.isEnabled = false
        schoolSelect.root.schoolSelectionBtn.isEnabled = false

        return schoolSelect.root
    }
}