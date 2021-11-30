package com.rudder.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityMainBinding
import com.rudder.databinding.FragmentClubJoinRequestDialogBinding
import com.rudder.ui.fragment.*
import com.rudder.util.FragmentShowHide
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_comment.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_show_post.*
import kotlinx.android.synthetic.main.fragment_show_post.postPreviewTailCommentCountTV
import kotlinx.android.synthetic.main.post_comments.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var mainBottomBarFragment: MainBottomBarFragment
    lateinit var addCommentFragment: AddCommentFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var myPageFragment: MyPageFragment
    private lateinit var addPostFragment: AddPostFragment
    private lateinit var searchPostFragment: SearchPostFragment
    lateinit var showPostFragment: ShowPostFragment
    lateinit var communityPostBottomSheetFragment: CommunityPostBottomSheetFragment
    lateinit var communityCommentBottomSheetFragment: CommunityCommentBottomSheetFragment
    lateinit var communityPostReportFragment: CommunityPostReportFragment
    lateinit var communityCommentReportFragment: CommunityCommentReportFragment
    private lateinit var clubJoinRequestDialogFragment: ClubJoinRequestDialogFragment
    lateinit var communityCommentEditFragment: CommunityCommentEditFragment

    private lateinit var contactUsFragment: ContactUsFragment

    private lateinit var categorySelectMyPageFragment: CategorySelectMyPageFragment


    lateinit var editPostFragment: EditPostFragment

    private val purpleRudder by lazy { ContextCompat.getColor(this, R.color.purple_rudder) }
    private val grey by lazy { ContextCompat.getColor(this, R.color.grey) }
    private val black by lazy { ContextCompat.getColor(this, R.color.black) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
        binding.mainVM = viewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialog.setMessage("Please wait ...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressBar.bringToFront()

        mainBottomBarFragment = MainBottomBarFragment()
        communityFragment = CommunityFragment()
        myPageFragment = MyPageFragment()
        addPostFragment = AddPostFragment()

        showPostFragment = ShowPostFragment(viewModel)
        addCommentFragment = AddCommentFragment(viewModel)
        communityPostBottomSheetFragment = CommunityPostBottomSheetFragment(viewModel)
        communityCommentBottomSheetFragment = CommunityCommentBottomSheetFragment(viewModel)
        communityPostReportFragment = CommunityPostReportFragment(viewModel)
        communityCommentReportFragment = CommunityCommentReportFragment(viewModel)
        clubJoinRequestDialogFragment = ClubJoinRequestDialogFragment()
        communityCommentEditFragment = CommunityCommentEditFragment(viewModel)
        contactUsFragment = ContactUsFragment()
        editPostFragment = EditPostFragment(viewModel)
        categorySelectMyPageFragment = CategorySelectMyPageFragment()
        searchPostFragment = SearchPostFragment()



        val toastDeletePostComplete = Toast.makeText(
            this,
            "Delete Post Complete!",
            Toast.LENGTH_LONG
        )
        val toastDeleteCommentComplete = Toast.makeText(
            this,
            "Delete Comment Complete!",
            Toast.LENGTH_LONG
        )
        val toastStringBlank = Toast.makeText(
            this,
            "Content can not be blank.",
            Toast.LENGTH_LONG
        )


        supportFragmentManager.beginTransaction()
            .add(R.id.mainBottomBar, mainBottomBarFragment,"mainBottomBar")
            .add(R.id.mainDisplay, myPageFragment, "myPage")
            .hide(myPageFragment)
            .add(R.id.mainDisplay, communityFragment, "community")
            .commit()













        viewModel.isContactUs.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {
                if (!contactUsFragment.isAdded)
                    contactUsFragment.show(
                        supportFragmentManager,
                        contactUsFragment.tag
                    )
            }
        })










        viewModel.isClubJoinRequest.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {
                if (!clubJoinRequestDialogFragment.isAdded)
                    clubJoinRequestDialogFragment.show(
                        supportFragmentManager,
                        clubJoinRequestDialogFragment.tag
                    )
            }
        })




        viewModel.selectedTab.observe(this, Observer {
            when (it) {
                R.id.communityButton -> {
                    FragmentShowHide(supportFragmentManager).showFragment(
                        communityFragment,
                        R.id.mainDisplay
                    )
                    changeColorCommunity()
                }
                R.id.myPageButton -> {
                    FragmentShowHide(supportFragmentManager).showFragment(
                        myPageFragment,
                        R.id.mainDisplay
                    )
                    changeColorMyPage()
                }
            }
        })

        viewModel.isAddPostClick.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {

                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.addToBackStack()

                fragmentShowHide.hideFragment(mainBottomBarFragment)

                fragmentShowHide.addFragment(addPostFragment, R.id.mainDisplay, "addPost")
                fragmentShowHide.showFragment(addPostFragment, R.id.mainDisplay)
                viewModel.clearAddPost()
            }
        })

        viewModel.isSearchPostClick.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.addToBackStack()
                fragmentShowHide.hideFragment(mainBottomBarFragment)

                fragmentShowHide.addFragment(searchPostFragment, R.id.mainDisplay, "searchPost")
                fragmentShowHide.showFragment(searchPostFragment, R.id.mainDisplay)

            }
        })







        viewModel.startLoginActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                StartActivityUtil.callActivity(this, LoginActivity())
                finish()
            }
        })



        viewModel.isAddPostSuccess.observe(this, Observer {
            super.onBackPressed()
        })











        viewModel.isContactUsSuccess.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    contactUsFragment.dismiss()
                }
            }
        })





        viewModel.isCancelClick.observe(this, Observer {
            event ->
            event.getContentIfNotHandled()?.let {
                if(it){
                    if (communityCommentEditFragment.isAdded)
                        communityCommentEditFragment.dismiss()
                    else if (contactUsFragment.isAdded)
                        contactUsFragment.dismiss()
                    else if (clubJoinRequestDialogFragment.isAdded)
                        clubJoinRequestDialogFragment.dismiss()
                }
            }

        })


        ProgressBarUtil.progressBarDialogFlag.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    progressDialog.show()
                    Log.d("progressBarDialogFlag", "progressDialog.show()")
                } else {
                    progressDialog.dismiss()
                    Log.d("progressBarDialogFlag", "progressDialog.dismiss()")
                }
            }
        })

        if (viewModel.noticeResponse.value == null) {
            viewModel.getNotice()
        }

        viewModel.noticeResponse.observe(this, Observer {
            it?.let {
                if (it.isExist) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Notice").setMessage(it.notice)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                    App.prefs.setValue("isNoticeAlreadyPopUp", "true")
                }

            }
        })


        viewModel.clickCategorySelect.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                    fragmentShowHide.addToBackStack()
                    fragmentShowHide.removeFragment(mainBottomBarFragment)

                    fragmentShowHide.addFragment(
                        categorySelectMyPageFragment,
                        R.id.mainDisplay,
                        "categorySelectMyPageFragment"
                    )
                    fragmentShowHide.showFragment(categorySelectMyPageFragment, R.id.mainDisplay)

                }
            }
        })

        viewModel.categorySelectApply.observe(this, Observer { // Apply 버튼
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    viewModel.getSelectedCategories()
                    onBackPressed()
                }
            }
        })


        viewModel.isStringBlank.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastStringBlank.show()
                }
            }
        })


    }

    // id가 명시되어있지 않은 다른 부분을 터치했을 때 키보드가 보여져있는 상태면 키보드를 내림.
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        Log.d("onBackPressed", "onBackPressed")
        try {
            val isBackButtonAvailable =
                (!supportFragmentManager.findFragmentByTag("myPage")!!.isVisible) && (!supportFragmentManager.findFragmentByTag(
                    "community"
                )!!.isVisible)
            if (isBackButtonAvailable) { // 마이페이지 or 커뮤니티화면 아닐 때만 back버튼 활성화
                Log.d("call",addCommentFragment.isVisible.toString())

                super.onBackPressed()
                if (addCommentFragment.isVisible) {
                    swapMainBottomBar()
                }
            } else {
                moveTaskToBack(true)
            }

            if (viewModel.selectedTab.value == R.id.myPageButton) {
                changeColorMyPage()
            }

            if (showPostFragment.isVisible) {
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.hideFragment(mainBottomBarFragment)
                fragmentShowHide.showFragment(addCommentFragment, R.id.mainBottomBar)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }


    }


    fun getDisplaySize(): ArrayList<Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = this@MainActivity.windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left - insets.right
            val height = windowMetrics.bounds.height() - insets.top - insets.bottom
            arrayListOf(width, height)
        } else {
            val displayMertrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMertrics)
            val width = displayMertrics.widthPixels
            val height = displayMertrics.heightPixels
            arrayListOf(width, height)
        }
    }

    fun swapMainBottomBar() {
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        if(searchPostFragment.isVisible){
            fragmentShowHide.hideAllFragment(R.id.mainBottomBar)
        }else{
            fragmentShowHide.showFragment(mainBottomBarFragment, R.id.mainBottomBar)
        }

    }


    fun showParentCommentInfo() {
        parentCommentInfo.visibility = View.VISIBLE
    }

    fun hideParentCommentInfo() {
        parentCommentInfo.visibility = View.GONE
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun expandProgressBarAnimation() {

    }

    fun changeSelectedPostPosition(position: Int) {
        viewModel.setSelectedPostPosition(position)
    }

    fun showPost(viewModel: MainViewModel,showPostFragment: ShowPostFragment) {
        viewModel.isLikePost()
        this.showPostFragment = showPostFragment

        parentCommentInfoClose.setOnClickListener {
            this.showPostFragment.closeParentCommentInfo()
        }
        Log.d("has",parentCommentInfoClose.hasOnClickListeners().toString())
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)

        fragmentShowHide.addToBackStack()
        fragmentShowHide.addFragment(this.showPostFragment, R.id.mainDisplay, "showPost")
        fragmentShowHide.showFragment(this.showPostFragment, R.id.mainDisplay)
    }


    fun showAddComment(addCommentFragment: AddCommentFragment) {
        this.addCommentFragment = addCommentFragment
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        fragmentShowHide.addFragment(this.addCommentFragment, R.id.mainBottomBar, "addComment")
        fragmentShowHide.showFragment(this.addCommentFragment, R.id.mainBottomBar)
    }


    fun changeColorCommunity() {
        mainBottomBarFragment.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.myPageIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
    }

    fun changeColorMyPage() {
        mainBottomBarFragment.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
        mainBottomBarFragment.communityIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
    }


    fun validateBack(tag: String): Boolean {
        return if (supportFragmentManager.findFragmentByTag(tag)==null){
            false
        }else{
            supportFragmentManager.findFragmentByTag(tag)!!.isVisible
        }
    }

    fun showPostMore(communityPostBottomSheetFragment: CommunityPostBottomSheetFragment){
        this.communityPostBottomSheetFragment = communityPostBottomSheetFragment
        if (!communityPostBottomSheetFragment.isAdded)
            communityPostBottomSheetFragment.show(
                supportFragmentManager,
                communityPostBottomSheetFragment.tag
            )
    }

    fun setParentCommentInfoText(string: String){
        parentCommentInfoTextTextView.text = string
    }

    fun showCommentMore(communityCommentBottomSheetFragment: CommunityCommentBottomSheetFragment){
        this.communityCommentBottomSheetFragment = communityCommentBottomSheetFragment
        if (!this.communityCommentBottomSheetFragment.isAdded)
            this.communityCommentBottomSheetFragment.show(
                supportFragmentManager,
                this.communityCommentBottomSheetFragment.tag
            )
    }

    fun closeCommunityBottomSheetFragment(){

        Toast.makeText(
            this,
            "Delete Comment Complete!",
            Toast.LENGTH_LONG
        ).show()
        communityCommentBottomSheetFragment.dismiss()
    }


}
