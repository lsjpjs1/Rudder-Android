package com.rudder.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivityMainBinding
import com.rudder.ui.fragment.*
import com.rudder.ui.fragment.comment.CommunityCommentBottomSheetFragment
import com.rudder.ui.fragment.comment.CommunityCommentEditFragment
import com.rudder.ui.fragment.comment.CommunityCommentReportFragment
import com.rudder.ui.fragment.community.CommunityDisplayFragment
import com.rudder.ui.fragment.post.*
import com.rudder.ui.fragment.search.SearchPostDisplayFragment
import com.rudder.util.ProgressBarUtil
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_comment.*
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.post_comments.*
import androidx.navigation.fragment.findNavController
import com.rudder.data.MainDisplayTab
import com.rudder.data.dto.NotificationType
import com.rudder.ui.fragment.community.CommunityDisplayFragmentDirections
import com.rudder.ui.fragment.mypage.*
import com.rudder.ui.fragment.notification.NotificationDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragmentDirections
import com.rudder.util.*
import com.rudder.viewModel.MainActivityViewModel
import com.rudder.viewModel.RequestCategoryViewModel
import com.rudder.viewModel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.show_post_display_image.view.*


class MainActivity : AppCompatActivity(), MainActivityInterface {
    val mainViewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val notificationViewModel : NotificationViewModel by lazy { ViewModelProvider(this).get(NotificationViewModel::class.java) }

    //private lateinit var mainActivityViewModel: MainActivityViewModel


    lateinit var communityDisplayFragment: CommunityDisplayFragment
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
    private lateinit var contactUsFragment: ContactUsFragment
    private lateinit var categorySelectMyPageFragment: CategorySelectMyPageFragment
    lateinit var requestCategoryBottomDialogFragment: RequestCategoryBottomDialogFragment
    lateinit var editPostFragment: EditPostFragment
    private lateinit var notificationDisplayFragment: NotificationDisplayFragment

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val navDisplayController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainDisplayContainerView)
            ?: throw IllegalStateException("the container MUST contain a fragment at least one")
        navHostFragment.findNavController()
    }

    private var notificationType:Int = -1
    private var itemId:Int = -1

    companion object {
        private const val KEY_SELECTED_TAB = "selectedTab"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityContainer.currentActivity = this
        //mainActivityViewModel = MainActivityViewModel()

        notificationType=intent.getIntExtra("notificationType",-1)
        itemId=intent.getIntExtra("itemId",-1)

        binding.mainVM = mainViewModel
        binding.lifecycleOwner = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val mainBottomNavigation: BottomNavigationView = binding.mainBottomNavigation
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

        savedInstanceState?.getInt(KEY_SELECTED_TAB)?.let {
                MainDisplayTab.from(it)
            }?.itemId?.let {
                mainBottomNavigation.selectedItemId = it
            }

        mainBottomNavigation.setOnNavigationItemSelectedListener {
            if (it.itemId != mainBottomNavigation.selectedItemId)
                NavigationUI.onNavDestinationSelected(it, navDisplayController)
            true
        }


        val progressDialog = ProgressDialog(this, R.style.MyAlertDialogStyle)
        progressDialog.setMessage("Please wait ...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal)


        communityDisplayFragment = CommunityDisplayFragment()
        myPageDisplayFragment = MyPageDisplayFragment()
        addPostDisplayFragment = AddPostDisplayFragment()
        showPostContentsFragment = ShowPostContentsFragment()
        communityPostBottomSheetFragment = CommunityPostBottomSheetFragment(mainViewModel)
        communityCommentBottomSheetFragment = CommunityCommentBottomSheetFragment(mainViewModel)
        communityPostReportFragment = CommunityPostReportFragment(mainViewModel)
        communityCommentReportFragment = CommunityCommentReportFragment(mainViewModel)
        clubJoinRequestDialogFragment = ClubJoinRequestDialogFragment()
        communityCommentEditFragment = CommunityCommentEditFragment(mainViewModel)
        contactUsFragment = ContactUsFragment()
        editPostFragment = EditPostFragment()
        categorySelectMyPageFragment = CategorySelectMyPageFragment()
        searchPostDisplayFragment = SearchPostDisplayFragment()
        notificationDisplayFragment = NotificationDisplayFragment()
        requestCategoryBottomDialogFragment = RequestCategoryBottomDialogFragment()


        val toastChooseValidCategory = Toast.makeText(
            this,
            "Choose a category.",
            Toast.LENGTH_LONG
        )
        val toastStringBlank = Toast.makeText(
            this,
            "One or more fields is blank!",
            Toast.LENGTH_LONG
        )


//        mainViewModel.isContactUs.observe(this, Observer {
//            if (it.getContentIfNotHandled()!!) {
//                if (!contactUsFragment.isAdded) {
//                    contactUsFragment.show(supportFragmentManager, contactUsFragment.tag)
//                }
//            }
//        })

        mainViewModel.isClubJoinRequest.observe(this, Observer {
            if (it.getContentIfNotHandled()!!) {
                if (!clubJoinRequestDialogFragment.isAdded)
                    clubJoinRequestDialogFragment.show(supportFragmentManager, clubJoinRequestDialogFragment.tag
                    )
            }
        })

        mainViewModel.startLoginActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                StartActivityUtil.callActivity(this, LoginActivity())
                finish()
            }
        })

        mainViewModel.isAddPostSuccess.observe(this, Observer {
            onBackPressed()
            mainBottomNavigationAppear()
        })


//        mainViewModel.isContactUsSuccess.observe(this, Observer {
//            it.getContentIfNotHandled()?.let { it ->
//                if (it) {
//                    contactUsFragment.dismiss()
//                }
//            }
//        })


        mainViewModel.isCancelClick.observe(this, Observer {
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
                } else {
                    progressDialog.dismiss()
                }
            }
        })


        mainViewModel.noticeResponse.observe(this, Observer {
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

        mainViewModel.categorySelectApply.observe(this, Observer { // Apply 버튼
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    mainViewModel.getSelectedCategories()
                    onBackPressed()
                }
            }
        })

        MainActivityViewModel.isStringBlank.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastStringBlank.show()
                }
            }
        })

        mainViewModel.isUnvalidCategorySelect.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    toastChooseValidCategory.show()
                }
            }
        })

        parentCommentInfoClose.setOnClickListener {
            mainViewModel.clearNestedCommentInfo()

        }

        if (notificationType != -1 && itemId != -1) {
            moveByNotificationType(notificationType,itemId)
        } else {
             if (mainViewModel.noticeResponse.value == null) {
                 mainViewModel.getNotice()
            }
        }
    }

    private fun moveByNotificationType(notificationType : Int, itemId: Int){
        when (notificationType) {
            NotificationType.COMMENT.typeNumber, NotificationType.NESTED_COMMENT.typeNumber -> {
                notificationViewModel.getPostContentFromPostIdNotification(itemId)
                val action = CommunityDisplayFragmentDirections.actionNavigationCommunityToNavigationShowPost(notificationPostId = itemId, viewModelIndex = ShowPostDisplayFragment.NOTIFICATION_VIEW_MODEL)
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    navDisplayController.navigate(action)
                }, 300) // delay를 주지 않으면, postmessage와 postmessageRoom 두 개의 view가 바로 그려져서 겹쳐져 보이게 되기에 delay를 줌.

                mainBottomNavigationDisappear()
            }

            NotificationType.POST_MESSAGE.typeNumber -> {
                val navController = navDisplayController
                val actionNotificationToPostMessage = CommunityDisplayFragmentDirections.actionNavigationCommunityToNavigationPostmessage(notificationPostMessageRoomId = itemId)
                val actionPostMessageToPostMessageRoom = PostMessageDisplayFragmentDirections.actionNavigationPostmessageToNavigationPostmessageRoom(postMessageRoomId = itemId)
                navController.navigate(actionNotificationToPostMessage)

                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    navController.navigate(actionPostMessageToPostMessageRoom)
                }, 300) // delay를 주지 않으면, postmessage와 postmessageRoom 두 개의 view가 바로 그려져서 겹쳐져 보이게 되기에 delay를 줌.

                mainBottomNavigationDisappear()
            }
        }
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

    fun showParentCommentInfo() {
        parentCommentInfo.visibility = View.VISIBLE
    }

    fun hideParentCommentInfo() {
        parentCommentInfo.visibility = View.GONE
    }


    fun showPostMore(communityPostBottomSheetFragment: CommunityPostBottomSheetFragment){
        this.communityPostBottomSheetFragment = communityPostBottomSheetFragment

        if (!communityPostBottomSheetFragment.isVisible && !communityPostBottomSheetFragment.isAdded) {
            communityPostBottomSheetFragment.show(supportFragmentManager,communityPostBottomSheetFragment.tag)
        }
    }

    fun setParentCommentInfoText(string: String){
        parentCommentInfoTextTextView.text = string
    }

    fun showCommentMore(communityCommentBottomSheetFragment: CommunityCommentBottomSheetFragment){
        this.communityCommentBottomSheetFragment = communityCommentBottomSheetFragment
        if (!this.communityCommentBottomSheetFragment.isAdded) {
            this.communityCommentBottomSheetFragment.show(supportFragmentManager, this.communityCommentBottomSheetFragment.tag)
        }
    }


    fun showRequestCategoryBottomDialog(){
        if (!this.requestCategoryBottomDialogFragment.isAdded) {
            this.requestCategoryBottomDialogFragment.show(supportFragmentManager, this.requestCategoryBottomDialogFragment.tag)
        }
    }

    fun closeCommunityBottomSheetFragment(){
        Toast.makeText(
            this,
            "Delete Comment Complete!",
            Toast.LENGTH_LONG
        ).show()
        communityCommentBottomSheetFragment.dismiss()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_TAB, binding.mainBottomNavigation.selectedItemId)

    }

    fun mainBottomNavigationDisappear() {
        binding.mainBottomLayout.visibility = View.GONE
        val lp = binding.mainDisplayContainerView.layoutParams
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.mainDisplayContainerView.layoutParams = lp
    }

    fun mainBottomNavigationAppear() {
        binding.mainBottomLayout.visibility = View.VISIBLE
        if (binding.mainBottomNavigation.visibility == View.GONE) {
            binding.mainBottomNavigation.visibility = View.VISIBLE
        }
        val lp = binding.mainDisplayContainerView.layoutParams
        lp.height = 0
        binding.mainDisplayContainerView.layoutParams = lp
    }

    fun nestedCommentDisappear(){
        mainViewModel.clearNestedCommentInfo()
    }


    override fun showPostMessageRoomFragment(postMessageRoomId: Int) {
        val action = PostMessageDisplayFragmentDirections.actionNavigationPostmessageToNavigationPostmessageRoom(postMessageRoomId)
        navDisplayController.navigate(action)
    }

    override fun onDestroy() {
        ActivityContainer.clearCurrentActivity(this)
        super.onDestroy()
    }


    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        navDisplayController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_community -> {
                    mainBottomNavigationAppear()
                    nestedCommentDisappear()
                }
                R.id.navigation_postmessage -> {
                    mainBottomNavigationAppear()
                }
                R.id.navigation_mypage -> {
                    mainBottomNavigationAppear()
                }
                R.id.navigation_search -> {
                    mainBottomNavigationDisappear()
                    nestedCommentDisappear()
                }
                R.id.navigation_jobs -> {
                    mainBottomNavigationAppear()
                }
                R.id.navigation_my_post -> {
                    mainBottomNavigationDisappear()
                    nestedCommentDisappear()
                }
                else -> {
                }
            }
        }

        super.onBackPressed()
    }

}
