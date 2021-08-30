package com.rudder.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.rudder.R
import com.rudder.databinding.FragmentCategorySelectBinding
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_category_select.*
import kotlinx.android.synthetic.main.fragment_category_select.view.*


class CategorySelectFragment : Fragment() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var categorySelectBindinginding : FragmentCategorySelectBinding

    val stringList: ArrayList<String> = arrayListOf<String>("Hello", "Kotlin", "Wow","Hello", "Kotlin", "Wow","Hello", "Kotlin", "Wow","Hello", "Kotlin", "Wow")

    fun setCategoryChips(categorys : ArrayList<String> ) {
        for ( category in categorys ) {
            val mChip = this.layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip
            mChip.text = category
            //val paddingDp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10L, resources.displayMetrics) as Int
            mChip.setPadding(100, 50, 50, 50)
            categorySelectBindinginding.root.chipsPrograms.addView(mChip)
        }

    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categorySelectBindinginding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_select,container,false)
        categorySelectBindinginding.signUpVM = viewModel
        categorySelectBindinginding.lifecycleOwner = this


        setCategoryChips(stringList)

        return categorySelectBindinginding.root
    }


}