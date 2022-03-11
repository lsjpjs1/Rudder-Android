package com.rudder.ui.fragment.mypage

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rudder.R
import com.rudder.data.remote.Category
import com.rudder.databinding.FragmentMyPageCategorySelectBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_my_page_category_select.view.*
import java.util.*


class CategorySelectMyPageFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var fragmentMyPageCategorySelectBinding : FragmentMyPageCategorySelectBinding

    private val lazyContext by lazy {
        requireContext()
    }

    companion object{
        const val TAG = "CategorySelectMyPageFragment"
    }


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMyPageCategorySelectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_category_select,container,false)
        fragmentMyPageCategorySelectBinding.mainVM = mainViewModel
        fragmentMyPageCategorySelectBinding.lifecycleOwner = this

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        val chipWidth = (displayDpValue[0] * 0.42).toInt()
        val chipHeight = (displayDpValue[1] * 0.09).toInt()
        val commonCategoryList = arrayListOf<Category>()
        val departmentACategoryList = arrayListOf<Category>()
        val departmentBCategoryList = arrayListOf<Category>()

        mainViewModel.allCategories.observe(viewLifecycleOwner, Observer {
            for ( i in 0 until it.size ) {
                if (it[i].categoryType == "department") {
                    departmentACategoryList.add(it[i])
                    departmentBCategoryList.add(it[i])
                } else if (it[i].categoryType == "common") {
                    commonCategoryList.add(it[i])
                }
            }
            setCategoryChips(commonCategoryList,chipWidth, chipHeight,R.layout.item_chip_category,fragmentMyPageCategorySelectBinding.root.chipsPrograms)

            if (departmentACategoryList.map{it.categoryName}[0] != "Choose Your Department A") {
                departmentACategoryList.add(0, Category(categoryName = "Choose university Department A", isMember = null, categoryId = -1, categoryType = "dummy_select",categoryAbbreviation = "Choose university Department A") )
            }

            if (departmentBCategoryList.map{it.categoryName}[0] != "Choose Your Department B") {
                departmentBCategoryList.add(0, Category(categoryName = "Choose university Department B", isMember = null, categoryId = -1, categoryType = "dummy_select",categoryAbbreviation = "Choose university Department B") )
            }

            val departmentASpinnerAdapter = ArrayAdapter(lazyContext, R.layout.custom_spinner_layout, departmentACategoryList)
            val departmentBSpinnerAdapter = ArrayAdapter(lazyContext, R.layout.custom_spinner_layout, departmentBCategoryList)
            fragmentMyPageCategorySelectBinding.departmentASpinner.adapter = departmentASpinnerAdapter
            fragmentMyPageCategorySelectBinding.departmentBSpinner.adapter = departmentBSpinnerAdapter
        })

        mainViewModel.allClubCategories.observe(viewLifecycleOwner, Observer {
            it?.let {
                fragmentMyPageCategorySelectBinding.root.chipsProgramsClub.removeAllViews()
                setCategoryChips(
                    it,
                    chipWidth,
                    chipHeight,
                    R.layout.item_chip_category,
                    fragmentMyPageCategorySelectBinding.root.chipsProgramsClub
                )
            }
        })

        val lp = fragmentMyPageCategorySelectBinding.requestCategoryChip.layoutParams
        lp.height = chipHeight
        lp.width = chipWidth
        fragmentMyPageCategorySelectBinding.root.requestCategoryChip.layoutParams = lp

        fragmentMyPageCategorySelectBinding.requestCategoryChip.setOnClickListener {
            (activity as MainActivity).showRequestCategoryBottomDialog()
        }

        fragmentMyPageCategorySelectBinding.categoryBackBtn.setOnClickListener { view ->
            view.findNavController().popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }

        return fragmentMyPageCategorySelectBinding.root
    }


    override fun onResume() {
        super.onResume()
        mainViewModel.clearDepartmentCategory()
    }


    fun setCategoryChips(categories : ArrayList<Category>,width : Int, height : Int, layoutResource : Int, chipGroup: ChipGroup ) {
        for ( i in 0 until categories.size ) {
            val mChip = this.layoutInflater.inflate(layoutResource, null, false) as Chip
            mChip.width = width
            mChip.height = height
            mChip.text = categories[i].categoryName
            mChip.tag = categories[i].categoryId

            if(categories[i].isMember == null || categories[i].isMember ==  "t"){ // 동아리원인 경우
                mChip.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, boolean ->
                    mainViewModel.categoryIdSelect(compoundButton.tag.toString().toInt(), boolean)
                })
            } else if (categories[i].isMember == "r" ) { // 가입 신청은 해서, 대기중
                mChip.isCheckedIconVisible = false
                mChip.isChipIconVisible = false
                mChip.text = mChip.text.toString()+" (pending)"
                mChip.isCheckable = false
            } else { // 동아리원이 아닌 경우
                mChip.isCheckedIconVisible = false
                mChip.isChipIconVisible = false
                mChip.text = "Join " +  mChip.text.toString()
                mChip.isCheckable = false
                mChip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(lazyContext, R.color.purple_100))
                mChip.setOnClickListener {
                    mainViewModel.setSelectedRequestJoinClubCategoryId(categories[i].categoryId)
                    mainViewModel.clickClubJoinRequest()
                }
            }
            chipGroup.addView(mChip)
        }
    }
}