package com.rudder.ui.fragment.mypage

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

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var fragmentMyPageCategorySelectBinding : FragmentMyPageCategorySelectBinding

    private val lazyContext by lazy {
        requireContext()
    }

    companion object{
        const val TAG = "CategorySelectMyPageFragment"
    }



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMyPageCategorySelectBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_category_select,container,false)
        fragmentMyPageCategorySelectBinding.mainVM = viewModel
        fragmentMyPageCategorySelectBinding.lifecycleOwner = this


        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        val chipWidth = (displayDpValue[0] * 0.42).toInt()
        val chipHeight = (displayDpValue[1] * 0.09).toInt()

        viewModel.allCategories.observe(viewLifecycleOwner, Observer {
            setCategoryChips(viewModel.allCategories.value!!,chipWidth, chipHeight,R.layout.item_chip_category,fragmentMyPageCategorySelectBinding.root.chipsPrograms)
        })

        viewModel.allClubCategories.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("cate123",it.toString())
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

//        viewModel.isBackClick.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                if ((activity as MainActivity).validateBack("categorySelectMyPageFragment")){
//                    (activity as MainActivity).onBackPressed()
//                }
//            }
//        })

        fragmentMyPageCategorySelectBinding.categoryBackBtn.setOnClickListener { view ->
            view.findNavController().popBackStack()
        }


        return fragmentMyPageCategorySelectBinding.root
    }


    fun setCategoryChips(categories : ArrayList<Category>,width : Int, height : Int, layoutResource : Int, chipGroup: ChipGroup ) {
        for ( i in 0 until categories.size ) {
            val mChip = this.layoutInflater.inflate(layoutResource, null, false) as Chip

            mChip.width = width
            mChip.height = height
            mChip.text = categories[i].categoryName
            mChip.tag = categories[i].categoryId

            if(categories[i].isMember==null || categories[i].isMember=="t"){
                mChip.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, boolean ->
                    viewModel.categoryIdSelect(compoundButton.tag.toString().toInt(), boolean)
                })
            }else if(categories[i].isMember=="r"){
                mChip.isCheckedIconVisible = false
                mChip.isChipIconVisible = false
                mChip.text = mChip.text.toString()+" (pending)"
                mChip.isCheckable = false
            }else{
                // 동아리원이 아닌 경우
                mChip.isCheckedIconVisible = false
                mChip.isChipIconVisible = false
                mChip.text = "Join " +  mChip.text.toString()
                mChip.isCheckable = false
                mChip.setOnClickListener {
                    viewModel.setSelectedRequestJoinClubCategoryId(categories[i].categoryId)
                    viewModel.clickClubJoinRequest()
                }
            }


            chipGroup.addView(mChip)
        }

    }

}