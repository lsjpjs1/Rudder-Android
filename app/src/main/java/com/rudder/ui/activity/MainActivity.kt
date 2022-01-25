package com.rudder.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.fragment.*
import com.rudder.ui.fragment.comment.AddCommentFragment
import com.rudder.ui.fragment.comment.CommunityCommentBottomSheetFragment
import com.rudder.ui.fragment.comment.CommunityCommentEditFragment
import com.rudder.ui.fragment.comment.CommunityCommentReportFragment
import com.rudder.ui.fragment.community.CommunityDisplayFragment
import com.rudder.ui.fragment.mypage.CategorySelectMyPageFragment
import com.rudder.ui.fragment.mypage.ClubJoinRequestDialogFragment
import com.rudder.ui.fragment.mypage.ContactUsFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.post.*
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragment
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_comment.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.*
import kotlinx.android.synthetic.main.post_comments.*
import androidx.navigation.fragment.findNavController
import com.rudder.data.MainDisplayTab
import com.rudder.util.*
import kotlinx.android.synthetic.main.show_post_display_image.view.*


class MainActivity : AppCompatActivity(), MainActivityInterface {
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var mainBottomBarFragment: MainBottomBarFragment
    lateinit var addCommentFragment: AddCommentFragment
    private lateinit var communityDisplayFragment: CommunityDisplayFragment
    private lateinit var myPageDisplayFragment: MyPageDisplayFragment
    private lateinit var addPostDisplayFragment: AddPostDisplayFragment
    private lateinit var searchPostDisplayFragment: SearchPostDisplayFragment
    lateinit var showPostContentsFragment: ShowPostContentsFragment
    lateinit var communityPostBottomSheetFragment: CommunityPostBottomSheetFragment
    lateinit var communityCommentBottomSheetFragment: CommunityCommentBottomSheetFragment
    lateinit var communityPostReportFragment: CommunityPostReportFragment
    lateinit var communityCommentReportFragment: CommunityCommentReportFragment
    private lateinit var clubJoinRequestDialogFragment: ClubJoinRequestDialogFragment
    lateinit var communityCommentEditFragment: CommunityCommentEditFragment
    private lateinit var postMessageFragment: PostMessageDisplayFragment

    private lateinit var contactUsFragment: ContactUsFragment

    private lateinit var categorySelectMyPageFragment: CategorySelectMyPageFragment


    lateinit var editPostFragment: EditPostFragment

    private lateinit var notificationFragment: NotificationFragment

    private val purpleRudder by lazy { ContextCompat.getColor(this, R.color.purple_rudder) }
    private val grey by lazy { ContextCompat.getColor(this, R.color.grey) }
    private val black by lazy { ContextCompat.getColor(this, R.color.black) }



    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val navDisplayController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainDisplayContainerView)
            ?: throw IllegalStateException("the container MUST contain a fragment at least one")
        navHostFragment.findNavController()
    }


    companion object {
        private const val KEY_SELECTED_TAB = "selectedTab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

////
//        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
//            this,
//            R.layout.activity_main
//        )

        //val binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        binding.mainVM = viewModel
        binding.lifecycleOwner = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /////////////////////// 21 01 18 navigation
        val mainBottomNavigation: BottomNavigationView = binding.mainBottomNavigation
        //val navController = findNavController(R.id.mainDisplayContainerView)
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainDisplayContainerView) as NavHostFragment
        //val navController = navHostFragment.navController
        //mainBottomNavigation.setupWithNavController(navController)



        navDisplayController.apply {
            navigatorProvider.addNavigator(
                CustomBottomNavigator(
                    R.id.mainDisplayContainerView,
                    supportFragmentManager
                )
            )
            // set a graph at code not XML, because add a custom navigator
            setGraph(R.navigation.main_display_navigation_graph)

            mainBottomNavigation.setupWithNavController(this)
        }


        navDisplayController.navigatorProvider.addNavigator(FragmentNavigator(this,supportFragmentManager,R.id.mainDisplayContainerView))



        navDisplayController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_community -> {
                    Log.d("addonDes","community")
                    //supportActionBar?.hide()
                    //Timber.i("Navigation dest. changed: EditAddFragment. $supportActionBar")
                }
                R.id.navigation_postmessage -> {
                    Log.d("addonDes","postmessage")
                    //supportActionBar?.hide()
                    //Timber.i("Navigation dest. changed: EditAddFragment. $supportActionBar")
                }
                R.id.navigation_mypage -> {
                    Log.d("addonDes","mypage")
                    //supportActionBar?.hide()
                    //Timber.i("Navigation dest. changed: EditAddFragment. $supportActionBar")
                }

                else -> {
                    //supportActionBar?.show()
                    //Timber.i("Navigation dest. changed: else fragment. $supportActionBar")
                }
            }
        }



        savedInstanceState?.getInt(KEY_SELECTED_TAB)
            ?.let {
                MainDisplayTab.from(it)
            }
            ?.itemId
            ?.let {
                mainBottomNavigation.selectedItemId = it
            }

        mainBottomNavigation.setOnNavigationItemSelectedListener {
            if (it.itemId != mainBottomNavigation.selectedItemId)
                NavigationUI.onNavDestinationSelected(it, navDisplayController)
            true
        }

        //////////////////////


        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialog.setMessage("Please wait ...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)
        progressBar.bringToFront()
        mainBottomBarFragment = MainBottomBarFragment(this)
        communityDisplayFragment = CommunityDisplayFragment()
        myPageDisplayFragment = MyPageDisplayFragment()
        addPostDisplayFragment = AddPostDisplayFragment()

        showPostContentsFragment = ShowPostContentsFragment()
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
        searchPostDisplayFragment = SearchPostDisplayFragment()





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


//        supportFragmentManager.beginTransaction()
//            .add(R.id.mainBottomBar, mainBottomBarFragment,"mainBottomBar")
//            .add(R.id.mainDisplay, myPageDisplayFragment, "myPage")
//            .hide(myPageDisplayFragment)
//            .add(R.id.mainDisplay, communityDisplayFragment, "community")
//            .commit()

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


//
//        viewModel.selectedTab.observe(this, Observer {
//            when (it) {
//                R.id.communityButton -> {
//                    FragmentShowHide(supportFragmentManager).showFragment(
//                        communityDisplayFragment,
//                        R.id.mainDisplay
//                    )
//                    //changeColorCommunity()
//                }
//                R.id.myPageButton -> {
//                    FragmentShowHide(supportFragmentManager).showFragment(
//                        myPageDisplayFragment,
//                        R.id.mainDisplay
//                    )
//                    //changeColorMyPage()
//                }
//            }
//        })


//        viewModel.isAddPostClick.observe(this, Observer {
//            if (it.getContentIfNotHandled()!!) {
//
//                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
//                fragmentShowHide.addToBackStack()
//                fragmentShowHide.hideFragment(mainBottomBarFragment)
//                fragmentShowHide.hideFragment(communityDisplayFragment)
//                fragmentShowHide.addFragment(addPostDisplayFragment, R.id.mainDisplayContainerView, "addPost")
//                fragmentShowHide.showFragment(addPostDisplayFragment, R.id.mainDisplayContainerView)
//
//                viewModel.clearAddPost()
//            }
//        })

        viewModel.isSearchPostClick.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {
                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
                fragmentShowHide.addToBackStack()
                fragmentShowHide.hideFragment(communityDisplayFragment)
                //fragmentShowHide.hideFragment(mainDisplayContainerView)

//                fragmentShowHide.addFragment(searchPostDisplayFragment, R.id.mainDisplay, "searchPost")
//                fragmentShowHide.showFragment(searchPostDisplayFragment, R.id.mainDisplay)

                //navDisplayController.navigate(R.id.action_navigation_community_to_main_fragment_navigation_graph)
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

//    override fun onBackPressed() {
//        Log.d("onBackPressed", "onBackPressed")
//        try {
//            val isBackButtonAvailable =
//                (!supportFragmentManager.findFragmentByTag("myPage")!!.isVisible) && (!supportFragmentManager.findFragmentByTag(
//                    "community"
//                )!!.isVisible)
//            if (isBackButtonAvailable) { // 마이페이지 or 커뮤니티화면 아닐 때만 back버튼 활성화
//                Log.d("call",addCommentFragment.isVisible.toString())
//
//                super.onBackPressed()
//                if (addCommentFragment.isVisible) {
//                    //swapMainBottomBar()
//                }
//            } else {
//                moveTaskToBack(true)
//            }
//
//            if (viewModel.selectedTab.value == R.id.myPageButton) {
//                //changeColorMyPage()
//            }
//
//            if (showPostContentsFragment.isVisible) {
//                val fragmentShowHide = FragmentShowHide(supportFragmentManager)
//                //fragmentShowHide.hideFragment(mainBottomBarFragment)
//                //fragmentShowHide.showFragment(addCommentFragment, R.id.mainBottomBar)
//
//            }
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
//
//
//    }


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

//    fun swapMainBottomBar() {
//        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
//        if(searchPostDisplayFragment.isVisible){
//            fragmentShowHide.hideAllFragment(R.id.mainBottomBar)
//        }else{
//            fragmentShowHide.showFragment(mainBottomBarFragment, R.id.mainBottomBar)
//        }
//
//    }


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

    fun showPost(viewModel: MainViewModel, showPostContentsFragment: ShowPostContentsFragment) {
        viewModel.isLikePost()
        this.showPostContentsFragment = showPostContentsFragment

        parentCommentInfoClose.setOnClickListener {
            this.showPostContentsFragment.closeParentCommentInfo()
        }
        Log.d("has",parentCommentInfoClose.hasOnClickListeners().toString())
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)

        fragmentShowHide.addToBackStack()
        fragmentShowHide.addFragment(this.showPostContentsFragment, R.id.mainDisplay, "showPost")
        fragmentShowHide.showFragment(this.showPostContentsFragment, R.id.mainDisplay)
    }


//    fun showAddComment(addCommentFragment: AddCommentFragment) {
//        this.addCommentFragment = addCommentFragment
//        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
//        fragmentShowHide.addFragment(this.addCommentFragment, R.id.mainBottomBar, "addComment")
//        fragmentShowHide.showFragment(this.addCommentFragment, R.id.mainBottomBar)
//    }




//    fun changeColorCommunity() {
//        mainBottomBarFragment.communityIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
//        mainBottomBarFragment.myPageIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
//        mainBottomBarFragment.postMessagePageIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
//    }
//
//    fun changeColorMyPage() {
//        mainBottomBarFragment.myPageIcon.setColorFilter(purpleRudder, PorterDuff.Mode.SRC_IN)
//        mainBottomBarFragment.communityIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
//        mainBottomBarFragment.postMessagePageIcon.setColorFilter(black, PorterDuff.Mode.SRC_IN)
//    }

    ///

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

    fun showFragment(fragment: Fragment,id: Int, tag: String){
        val fragmentShowHide = FragmentShowHide(supportFragmentManager)
        fragmentShowHide.addFragment(fragment, id, tag)
        fragmentShowHide.showFragment(fragment,id)
    }

    override fun showNotificationFragment() {
        notificationFragment = NotificationFragment(this)
        showFragment(notificationFragment, R.id.mainDisplay,"notification")
    }

    override fun showPostMessageFragment() {
        postMessageFragment = PostMessageDisplayFragment()
        showFragment(postMessageFragment, R.id.mainDisplay, "postMessage")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_TAB, binding.mainBottomNavigation.selectedItemId)
    }


    fun mainBottomNavigationDisappear() {

        binding.mainBottomNavigation.visibility = View.INVISIBLE
        val lp = binding.mainDisplayContainerView.layoutParams
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.mainDisplayContainerView.layoutParams = lp
    }


    fun mainBottomNavigationAppear() {
        Log.d("funTmp2","funTmp2")
        binding.mainBottomNavigation.visibility = View.VISIBLE

//        val displaySizeHeight = getDisplaySize()[1]
//        val lp = binding.mainDisplayContainerView.layoutParams
//        lp.height = (displaySizeHeight * 0.93).toInt()
//        binding.mainDisplayContainerView.layoutParams = lp


//        binding.mainDisplayContainerView.updateLayoutParams<ConstraintLayout.LayoutParams> {
//            matchConstraintPercentHeight = 0.1f
//        }

//        val set = ConstraintSet()
//        set.constrainPercentHeight(R.id.mainDisplayContainerView, 0.5f)



    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("onBackMain","onBackMain")
    }

}
