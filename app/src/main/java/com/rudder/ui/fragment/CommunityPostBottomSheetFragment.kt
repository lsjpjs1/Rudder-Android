package com.rudder.ui.fragment


import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityPostBottomSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_community_post_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_school_select.*


class CommunityPostBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var communityPostBottomSheetBinding : FragmentCommunityPostBottomSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        communityPostBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_post_bottom_sheet, container,false)
        communityPostBottomSheetBinding.mainVM = viewModel
        communityPostBottomSheetBinding.lifecycleOwner = this

        viewModel.selectedPostMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityPostBottomSheetBinding.position = it
                Log.d("selectedPostMorePo","${communityPostBottomSheetBinding.position}")
            }
        })

        viewModel.isPostMine.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(!it) { // ismine == false, 내께 아니면
                    postBottomSheetCL2.visibility = View.GONE
                    postBottomSheetCL3.visibility = View.GONE

//                    var lp = communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams
//                    Log.d("height","${lp.height}")
//                    lp.height = 300
//                    communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp
                }
        }})

        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height

        var lp = communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams
        lp.height = (displayDpValue[1] * 0.1).toInt()

        communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams = lp
        //communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams = lp

        communityPostBottomSheetBinding.postBottomSheetCL3.layoutParams = lp
        communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp

        Log.d("height1","${postBottomSheetCL1.height}")
        Log.d("height3","${postBottomSheetCL3.height}")
        Log.d("height4","${postBottomSheetCL4.height}")





        return communityPostBottomSheetBinding.root
    }


}