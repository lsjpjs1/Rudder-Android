package com.rudder.ui.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentMyPageBinding
import com.rudder.databinding.FragmentSchoolSelectBinding
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.show_post_display_image.view.*

class MyPageFragment: Fragment() {


    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var myPageBinding : FragmentMyPageBinding
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myPageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page,container,false)
        myPageBinding.mainVM = viewModel
        myPageBinding.lifecycleOwner = this

        childFragmentManager.beginTransaction()
            .add(R.id.myPageHeader, MyPageHeaderFragment())
            .commit()

        viewModel.getMyProfileImageUrl()
        viewModel.myProfileImageUrl.value?.let {
            Log.d("myImage",it)
            Glide.with(myPageBinding.myProfileImageImageView.context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.raw.post_loading_raw)
                .into(myPageBinding.myProfileImageImageView)
        }
        viewModel.myProfileImageUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("myImage",it)
                Glide.with(myPageBinding.myProfileImageImageView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.raw.post_loading_raw)
                    .into(myPageBinding.myProfileImageImageView)
            }
        })


        return myPageBinding.root
    }



}