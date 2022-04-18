package com.rudder.data

import android.util.Log
import com.rudder.R
import com.rudder.ui.fragment.community.CommunityDisplayFragment
import com.rudder.ui.fragment.jobs.JobsContentsFragment
import com.rudder.ui.fragment.jobs.JobsDetailsFragment
import com.rudder.ui.fragment.jobs.JobsSavedFragment
import com.rudder.ui.fragment.mypage.CategorySelectMyPageFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.mypage.MyPostDisplayFragment
import com.rudder.ui.fragment.mypage.SettingFragment
import com.rudder.ui.fragment.notification.NotificationDisplayFragment
import com.rudder.ui.fragment.post.AddPostDisplayFragment
import com.rudder.ui.fragment.post.EditPostFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageRoomFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragment


enum class MainDisplayTab( // Just, show, hide
    val itemId: Int,
    val tag: String
) {
    COMMUNITY(R.id.navigation_community, CommunityDisplayFragment.TAG),
    JOBS(R.id.navigation_jobs, JobsContentsFragment.TAG),
    POSTMESSAGE(R.id.navigation_postmessage, PostMessageDisplayFragment.TAG),
    MYPAGE(R.id.navigation_mypage, MyPageDisplayFragment.TAG),
    SEARCH(R.id.navigation_search, SearchPostDisplayFragment.TAG),
    ADDPOST(R.id.navigation_add_post, AddPostDisplayFragment.TAG),
    SHOWPOST(R.id.navigation_show_post, ShowPostDisplayFragment.TAG),
    CATEGORYSELECTMYPAGE(R.id.navigation_category_select_my_page, CategorySelectMyPageFragment.TAG),
    EDITPOST(R.id.navigation_edit_post, EditPostFragment.TAG),
    POSTMESSAGEROOM(R.id.navigation_postmessage_room, PostMessageRoomFragment.TAG),
    MYPOST(R.id.navigation_my_post, MyPostDisplayFragment.TAG),
    NOTIFICATION(R.id.navigation_notification, NotificationDisplayFragment.TAG),
    SETTING(R.id.navigation_setting, SettingFragment.TAG),
    JOBSDETAILS(R.id.navigation_jobs_details, JobsDetailsFragment.TAG),
    JOBSSAVED(R.id.navigation_jobs_saved, JobsSavedFragment.TAG);

    companion object {
        fun from(itemId: Int): MainDisplayTab? = values().firstOrNull { it.itemId == itemId }
    }
}


fun MainDisplayTab.Companion.otherTab(exceptTag: String): Sequence<MainDisplayTab> =
    MainDisplayTab.values()
        .asSequence()
        .filter {//
            it.tag != exceptTag
        }
