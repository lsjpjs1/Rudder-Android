package com.rudder.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.rudder.R
import com.rudder.databinding.FragmentSignUpCategorySelectBinding
import com.rudder.ui.activity.SignUpActivity
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_sign_up_category_select.view.*
import java.util.*
import kotlin.collections.ArrayList


class CategorySelectSignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var categorySelectBindinginding : FragmentSignUpCategorySelectBinding


    fun setCategoryChips(categorys : ArrayList<String>, categoryId : ArrayList<Int> ,width : Int, height : Int ) {
        for ( i in 0 until categorys.size ) {
            val mChip = this.layoutInflater.inflate(R.layout.item_chip_category, null, false) as Chip

            mChip.width = width
            mChip.height = height
            mChip.text = categorys[i]
            mChip.tag = categoryId[i]

            mChip.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, boolean ->
                viewModel.categoryIdSelect(compoundButton.tag.toString().toInt(), boolean)
            })


            categorySelectBindinginding.root.chipsPrograms.addView(mChip)
        }

    }



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categorySelectBindinginding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_category_select,container,false)
        categorySelectBindinginding.signUpVM = viewModel
        categorySelectBindinginding.lifecycleOwner = this


        val displayDpValue = (activity as SignUpActivity).getDisplaySize() // [0] == width, [1] == height
        val chipWidth = (displayDpValue[0] * 0.42).toInt()
        val chipHeight = (displayDpValue[1] * 0.1).toInt()

        viewModel.categoryNames.observe(viewLifecycleOwner, Observer {
            setCategoryChips(viewModel.categoryNames.value!!, viewModel.categoryIdAllList.value!!, chipWidth, chipHeight)
        })



        return categorySelectBindinginding.root
    }


}