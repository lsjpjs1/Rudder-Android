package com.rudder.ui.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.rudder.R
import com.rudder.databinding.FragmentCategorySelectBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.activity.SignUpActivity
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_category_select.view.*
import kotlinx.android.synthetic.main.fragment_school_select.*


class CategorySelectFragment : Fragment() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var categorySelectBindinginding : FragmentCategorySelectBinding
    //val displayDpValue = (activity as SignUpActivity).getDisplaySize() // [0] == width, [1] == height


    fun setCategoryChips(categorys : ArrayList<String> ) {
        for ( category in categorys ) {
            val mChip = this.layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip

//            mChip.height = 160
//            mChip.width = 160
//
//            mChip.text = category

//            mChip.height = 160
//            mChip.width = 200
            //mChip.id = R.id.test_id_01
            //val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10L, resources.displayMetrics) as Int
            //mChip.setPadding(1000, 500, 50, 50)

//            var lp1 = mChip.layoutParams
//            lp1.height = (displayDpValue[1] * 0.1).toInt()
            categorySelectBindinginding.root.chipsPrograms.addView(mChip)
        }

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categorySelectBindinginding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_select,container,false)
        categorySelectBindinginding.signUpVM = viewModel
        categorySelectBindinginding.lifecycleOwner = this


        viewModel.categoryNames.observe(viewLifecycleOwner, Observer {
            Log.d("categoryNames_fragment","${viewModel.categoryNames.value!!}")
            setCategoryChips(viewModel.categoryNames.value!!)
        })


        return categorySelectBindinginding.root
    }
}