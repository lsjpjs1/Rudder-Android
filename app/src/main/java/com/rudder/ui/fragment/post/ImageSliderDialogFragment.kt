package com.rudder.ui.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.rudder.R
import com.rudder.databinding.FragmentImageSliderDialogBinding
import com.rudder.databinding.FragmentSendPostMessageDialogBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.FullImagesAdapter
import com.rudder.util.CirclePagerIndicatorDecoration

class ImageSliderDialogFragment(val imageUrlList: ArrayList<String>,val selectedIndex: Int) : DialogFragment() {

    companion object{
        const val TAG = "ImageSliderDialogFragment"
        fun instance(imageUrlList: ArrayList<String>,selectedIndex: Int) : ImageSliderDialogFragment {
            return ImageSliderDialogFragment(imageUrlList, selectedIndex)
        }
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }

    private val lazyContext by lazy {
        requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentImageSliderDialogBinding>(inflater, R.layout.fragment_image_slider_dialog, container,false)
        val adapter = FullImagesAdapter()
        adapter.submitList(imageUrlList)
        val displayDpValue = parentActivity.getDisplaySize() // [0] == width, [1] == height
        val snapHelper = PagerSnapHelper()
        binding.fullImageRV.also {
            it.layoutManager = LinearLayoutManager(lazyContext,LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }
        binding.fullImageRV.addItemDecoration(
            CirclePagerIndicatorDecoration()
        )
        snapHelper.attachToRecyclerView(binding.fullImageRV)
        binding.fullImageRV.scrollToPosition(selectedIndex)
        var lp = binding.imageSliderCL.layoutParams
        lp.height = (displayDpValue[1] * 0.8).toInt()
        lp.width = (displayDpValue[0] * 0.9).toInt()
        binding.imageSliderCL.layoutParams = lp
        return binding.root
    }


}