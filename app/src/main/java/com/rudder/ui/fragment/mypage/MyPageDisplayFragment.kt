package com.rudder.ui.fragment.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.databinding.FragmentMyPageDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.MyPageFragmentInterface
import com.rudder.util.uiUtils.PercentDivide
import com.rudder.util.uiUtils.PercentDivideImpl
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_webview_modal.view.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_terms_of_service.view.*
import kotlinx.android.synthetic.main.show_post_display_image.view.*


class MyPageDisplayFragment: Fragment(), MyPageFragmentInterface {


    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var editNicknameDialogFragment: EditNicknameDialogFragment
    private lateinit var editProfileImageDialogFragment: EditProfileImageDialogFragment
    private lateinit var myPageBinding : FragmentMyPageDisplayBinding

    companion object{
        const val TAG = "MyPageDisplayFragment"
    }



    private val lazyContext by lazy {
        requireContext()
    }

    private val parentActivity by lazy{
        activity as MainActivity
    }

    //var myPageBinding: FragmentMyPageDisplayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        myPageBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_page_display,
            container,
            false
        )

        myPageBinding.mainVM = viewModel
        myPageBinding.myPageDisplayFragment = this
        myPageBinding.lifecycleOwner = viewLifecycleOwner

        val divideChildTarget = myPageBinding.constraintLayoutMyPage1
        val displaySize = parentActivity.getDisplaySize()
        val percentDivide : PercentDivide = PercentDivideImpl(divideChildTarget,displaySize,0.4f)
        percentDivide.divideChildSameRatio()

        childFragmentManager.beginTransaction()
            .add(R.id.myPageHeader, MyPageHeaderFragment())
            .commit()

        viewModel.myProfileImageUrl.value?.let {
            viewModel.getMyProfileImageUrl()
            Log.d("myImage", it)
            Glide.with(myPageBinding.myProfileImageImageView.context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.raw.post_loading_raw)
                .into(myPageBinding.myProfileImageImageView)
        }
        viewModel.myProfileImageUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("myImage", it)

                Glide.with(myPageBinding.myProfileImageImageView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.raw.post_loading_raw)
                    .into(myPageBinding.myProfileImageImageView)
            }
        })

        myPageBinding.constraintLayoutMyPage4.setOnClickListener {
            val dialogView: View = layoutInflater.inflate(R.layout.activity_webview_modal, null)
            val termsOfServiceURL = "https://sites.google.com/view/mateprivacyterms"
            dialogView.termsWebView.apply {
                webViewClient = WebViewClient()
                settings.builtInZoomControls = true
                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
            }

            dialogView.termsWebView.loadUrl(termsOfServiceURL)
            val builder: AlertDialog.Builder = AlertDialog.Builder(lazyContext)
            builder.setView(dialogView)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        return myPageBinding!!.root






    }

    fun showEditNicknameDialog(){
        editNicknameDialogFragment = EditNicknameDialogFragment()
        editNicknameDialogFragment.show(childFragmentManager,"editNicknameDialog")
    }

    fun showEditProfileImageDialog(){
        editProfileImageDialogFragment = EditProfileImageDialogFragment(this)
        editProfileImageDialogFragment.show(childFragmentManager, "editProfileImageDialog")
    }

    override fun setMyProfileImageUrl(url : String){
        viewModel.setMyProfileImageUrl(url)

    }

}