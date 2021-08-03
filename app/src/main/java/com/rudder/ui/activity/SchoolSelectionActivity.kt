package com.rudder.ui.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rudder.R
import kotlinx.android.synthetic.main.fragment_school_select.*

class SchoolSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_school_select)

        val items = resources.getStringArray(R.array.schools_array)

        schoolSelectSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.schools_array , R.layout.support_simple_spinner_dropdown_item)
            .also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            schoolSelectSpinner.adapter = adapter
            }

        schoolSelectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(ContentValues.TAG, "spinner 결과 : ${p2}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }
}