package com.rudder.ui.fragment

import android.os.Bundle
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
import com.rudder.databinding.FragmentMyPageCategorySelectBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_my_page_category_select.view.*
import kotlinx.android.synthetic.main.fragment_school_select.*
import java.util.*


class CategorySelectMyPageFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var fragmentMyPageCategorySelectBinding : FragmentMyPageCategorySelectBinding


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


            fragmentMyPageCategorySelectBinding.root.chipsPrograms.addView(mChip)
        }

    }



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMyPageCategorySelectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_category_select,container,false)
        fragmentMyPageCategorySelectBinding.mainVM = viewModel
        fragmentMyPageCategorySelectBinding.lifecycleOwner = this


        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        val chipWidth = (displayDpValue[0] * 0.4).toInt()
        val chipHeight = (displayDpValue[1] * 0.1).toInt()

        viewModel.categoryNamesForSelection.observe(viewLifecycleOwner, Observer {
            setCategoryChips(viewModel.categoryNamesForSelection.value!!, viewModel.categoryIdAllList.value!!, chipWidth, chipHeight)
        })


        return fragmentMyPageCategorySelectBinding.root
    }


}