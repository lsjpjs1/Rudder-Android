package com.rudder.ui.fragment.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentMyPageDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.AlertDialogFragment
import com.rudder.ui.fragment.MyPageFragmentInterface
import com.rudder.util.AlertDialogListener
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_webview_modal.view.*


class MyPageDisplayFragment: Fragment(), MyPageFragmentInterface {


    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var editNicknameDialogFragment: EditNicknameDialogFragment
    private lateinit var editProfileImageDialogFragment: EditProfileImageDialogFragment
    private lateinit var contactUsFragment: ContactUsFragment
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
        myPageBinding.constraintLayoutMyPage3.setOnClickListener {
            showLogoutAlert()
        }

        viewModel.getMyProfileImageUrl()

        viewModel.myProfileImageUrl.value?.let {
            Glide.with(myPageBinding.myProfileImageImageView.context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.raw.post_loading_raw)
                .into(myPageBinding.myProfileImageImageView)
        }

        viewModel.myProfileImageUrl.observe(viewLifecycleOwner, Observer {
            it?.let {

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

        myPageBinding.myPostConstraintLayout.setOnClickListener{ view ->
            val navController = view.findNavController()
            val action = MyPageDisplayFragmentDirections.actionNavigationMypageToNavigationMyPost(MyPostDisplayFragment.MY_POST)
            navController.navigate(action)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }

        myPageBinding.myCommentConstraintLayout.setOnClickListener{ view ->
            val navController = view.findNavController()
            val action = MyPageDisplayFragmentDirections.actionNavigationMypageToNavigationMyPost(MyPostDisplayFragment.MY_COMMENT)
            navController.navigate(action)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }

        myPageBinding.constraintLayoutMyPage6.setOnClickListener { view -> // search button click
            view.findNavController().navigate(R.id.action_navigation_mypage_to_navigation_category_select_my_page)
            (activity as MainActivity).mainBottomNavigationDisappear()
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

    fun showContactUsDialog(){
        contactUsFragment = ContactUsFragment()
        contactUsFragment.show(childFragmentManager, "editProfileImageDialog")
    }


    override fun setMyProfileImageUrl(url : String){
        viewModel.setMyProfileImageUrl(url)

    }


    fun showLogoutAlert(){
        val alertDialogFragment = AlertDialogFragment.instance(
            object : AlertDialogListener {
                override fun onOkClick() {
                    viewModel.callLoginOut()
                }
                override fun onCancelClick() {

                }
            },
            "Are you sure you want to logout?"
        )
        alertDialogFragment.show(childFragmentManager, AlertDialogFragment.TAG)
    }


}