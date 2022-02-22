package com.rudder.ui.fragment.post


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityPostBottomSheetBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.fragment.AlertDialogFragment
import com.rudder.ui.fragment.community.CommunityDisplayFragmentDirections
import com.rudder.ui.fragment.mypage.MyPostDisplayFragmentDirections
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragmentDirections
import com.rudder.ui.fragment.postmessage.SendPostMessageDialogFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragmentDirections
import com.rudder.util.AlertDialogListener
import com.rudder.viewModel.MainViewModel
import com.rudder.viewModel.MyCommentViewModel
import com.rudder.viewModel.MyPostViewModel
import com.rudder.viewModel.NotificationViewModel
import com.rudder.viewModel.SearchViewModel


class CommunityPostBottomSheetFragment(var viewModel: MainViewModel) : BottomSheetDialogFragment() {

    lateinit var communityPostBottomSheetBinding : FragmentCommunityPostBottomSheetBinding

    private val lazyContext by lazy {
        requireContext()
    }
    private val parentActivity by lazy {
        activity as MainActivity
    }



    private lateinit var sendPostMessageDialogFragment: SendPostMessageDialogFragment

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        communityPostBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_post_bottom_sheet, container,false)
        communityPostBottomSheetBinding.mainVM = viewModel
        communityPostBottomSheetBinding.lifecycleOwner = this
        communityPostBottomSheetBinding.communityPostBottomSheetFragment = this
        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        layoutConfig(displayDpValue)

        viewModel.selectedPostMorePosition.observe(viewLifecycleOwner, Observer {
            it?.let {
                communityPostBottomSheetBinding.position = it
            }
        })

        viewModel.isPostMine.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(!it) { // ismine == false, 내께 아니면
                    var lp4 = communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams
                    lp4.height = (displayDpValue[1] * 0.05).toInt()
                    communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp4

                    var lp1 = communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams
                    lp1.height = (displayDpValue[1] * 0.08).toInt()
                    communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams = lp1

                    var lp5 = communityPostBottomSheetBinding.postBottomSheetCL5.layoutParams
                    lp5.height = (displayDpValue[1] * 0.08).toInt()
                    communityPostBottomSheetBinding.postBottomSheetCL5.layoutParams = lp5

                    var lp6 = communityPostBottomSheetBinding.sendPostMessageCL.layoutParams
                    lp6.height = (displayDpValue[1] * 0.08).toInt()
                    communityPostBottomSheetBinding.sendPostMessageCL.layoutParams = lp6

                    communityPostBottomSheetBinding.postBottomSheetCL2.visibility = View.GONE
                    communityPostBottomSheetBinding.postBottomSheetCL3.visibility = View.GONE

                }else{
                    communityPostBottomSheetBinding.postBottomSheetCL1.visibility = View.GONE
                    communityPostBottomSheetBinding.postBottomSheetCL5.visibility = View.GONE
                    communityPostBottomSheetBinding.sendPostMessageCL.visibility = View.GONE
                }
        }})

        viewModel.isPostReport.observe(viewLifecycleOwner, Observer {
            event->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    if (!parentActivity.communityPostReportFragment.isAdded)
                        parentActivity.communityPostReportFragment = CommunityPostReportFragment(viewModel)
                    parentActivity.communityPostReportFragment.show(
                        parentActivity.supportFragmentManager,
                        parentActivity.communityPostReportFragment.tag
                    )
                }
            }

        })

//        viewModel.isPostEdit.observe(viewLifecycleOwner, Observer {
//            it?.let {
////                parentActivity.communityPostBottomSheetFragment.dismiss()
////
////                val fragmentShowHide = FragmentShowHide(parentActivity.supportFragmentManager)
////                fragmentShowHide.addToBackStack()
////                fragmentShowHide.hideFragment(parentActivity.mainBottomBarFragment)
////
////                if (parentActivity.addCommentFragment.isAdded) {
////                    fragmentShowHide.hideFragment(parentActivity.addCommentFragment)
////                }
////                parentActivity.editPostFragment = EditPostFragment(viewModel)
////                fragmentShowHide.addFragment(parentActivity.editPostFragment, R.id.mainDisplay, "editPost")
////                fragmentShowHide.showFragment(parentActivity.editPostFragment, R.id.mainDisplay)
//            }
//
//        })


        communityPostBottomSheetBinding.postMoreEditPostTextView.setOnClickListener { view ->
            parentActivity.communityPostBottomSheetFragment.dismiss()
            val navController = parentActivity.findNavController(R.id.mainDisplayContainerView)
            val currentDestination = navController.currentDestination?.label
            val previousDestination = navController.previousBackStackEntry?.destination?.label
            var action : NavDirections? = null

            if (currentDestination == "SearchPostDisplayFragment") {
                action = SearchPostDisplayFragmentDirections.actionNavigationSearchToNavigationEditPost(EditPostFragment.SEARCH_VIEW_MODEL)
            }  else if (previousDestination == "SearchPostDisplayFragment") {
                action = ShowPostDisplayFragmentDirections.actionNavigationShowPostToNavigationEditPost(EditPostFragment.SEARCH_VIEW_MODEL)
            }  else if(currentDestination == "Community") {
                action = CommunityDisplayFragmentDirections.actionNavigationCommunityToNavigationEditPost(EditPostFragment.MAIN_VIEW_MODEL)
            }  else if(previousDestination == "Community") {
                action = ShowPostDisplayFragmentDirections.actionNavigationShowPostToNavigationEditPost(EditPostFragment.MAIN_VIEW_MODEL)
            }  else if(previousDestination == "MyPostDisplayFragment") {
                if (viewModel is MyCommentViewModel) {
                    action = ShowPostDisplayFragmentDirections.actionNavigationShowPostToNavigationEditPost(EditPostFragment.MY_COMMENT_VIEW_MODEL)
                } else if (viewModel is MyPostViewModel) {
                    action = ShowPostDisplayFragmentDirections.actionNavigationShowPostToNavigationEditPost(EditPostFragment.MY_POST_VIEW_MODEL)
                }
            }  else if(currentDestination == "MyPostDisplayFragment") {
                if (viewModel is MyCommentViewModel) {
                    action = MyPostDisplayFragmentDirections.actionNavigationMyPostToNavigationEditPost(EditPostFragment.MY_COMMENT_VIEW_MODEL)
                } else if (viewModel is MyPostViewModel) {
                    action = MyPostDisplayFragmentDirections.actionNavigationMyPostToNavigationEditPost(EditPostFragment.MY_POST_VIEW_MODEL)
                }
            }  else if (previousDestination == "Notification") {
                    action = ShowPostDisplayFragmentDirections.actionNavigationShowPostToNavigationEditPost(EditPostFragment.NOTIFICATION_VIEW_MODEL)
            }

            navController.navigate(action!!)
            (activity as MainActivity).mainBottomNavigationDisappear()

        }


        return communityPostBottomSheetBinding.root
    }


    override fun onDismiss(dialog: DialogInterface) {
        viewModel.dismissPostMore()
        super.onDismiss(dialog)
    }

    private fun layoutConfig(displayDpValue : ArrayList<Int>) {
        var lp1 = communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams
        lp1.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL1.layoutParams = lp1

        var lp2 = communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams
        lp2.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL2.layoutParams = lp2

        var lp3 = communityPostBottomSheetBinding.postBottomSheetCL3.layoutParams
        lp3.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL3.layoutParams = lp3

        var lp4 = communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams
        lp4.height = (displayDpValue[1] * 0.05).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL4.layoutParams = lp4

        var lp5 = communityPostBottomSheetBinding.postBottomSheetCL5.layoutParams
        lp5.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.postBottomSheetCL5.layoutParams = lp5

        var lp6 = communityPostBottomSheetBinding.sendPostMessageCL.layoutParams
        lp6.height = (displayDpValue[1] * 0.08).toInt()
        communityPostBottomSheetBinding.sendPostMessageCL.layoutParams = lp6
    }

    fun showPostMessageDialog() {
        val receiveUserInfoId = viewModel.posts.value!![viewModel.selectedPostMorePosition.value!!].userInfoId
        sendPostMessageDialogFragment = SendPostMessageDialogFragment(receiveUserInfoId)
        sendPostMessageDialogFragment.show(childFragmentManager, "sendPostMessageDialogFragment")
    }

    fun showDeleteAlert(){
        val alertDialogFragment = AlertDialogFragment.instance(
            object : AlertDialogListener {
                override fun onOkClick() {
                    viewModel.clickPostDelete()
                }

                override fun onCancelClick() {

                }

            },
            "Do you really delete this post?"
        )

        alertDialogFragment.show(childFragmentManager, AlertDialogFragment.TAG)
    }

    fun showUserBlockAlert(){
            val alertDialogFragment = AlertDialogFragment.instance(
                object : AlertDialogListener {
                    override fun onOkClick() {
                        viewModel.clickBlockUser()
                    }

                    override fun onCancelClick() {

                    }

                },
                "Do you really block this user?"
            )

            alertDialogFragment.show(childFragmentManager,AlertDialogFragment.TAG)
    }



}