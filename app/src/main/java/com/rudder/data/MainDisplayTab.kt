package com.rudder.data

import android.util.Log
import com.rudder.R
import com.rudder.ui.fragment.community.CommunityDisplayFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment


enum class MainBottomTab(
    val itemId: Int,
    val tag: String
) {
    COMMUNITY(R.id.navigation_community, CommunityDisplayFragment.TAG),
    POSTMESSAGE(R.id.navigation_postmessage, PostMessageDisplayFragment.TAG),
    MYPAGE(R.id.navigation_mypage, MyPageDisplayFragment.TAG),

    SEARCH(R.id.navigation_search, "SearchPostDisplayFragment");


//    COMMUNITYHEADER(R.id.navigation_community, "CommunityHeaderFragment"),
//    POSTMESSAGEHEADER(R.id.navigation_postmessage, "PostMessageHeaderFragment"),
//    MYPAGEHEADER(R.id.navigation_mypage, "MyPageHeaderFragment");

    companion object {
        fun from(itemId: Int): MainBottomTab? = values().firstOrNull { it.itemId == itemId }
    }
}


fun MainBottomTab.Companion.otherTab(exceptTag: String): Sequence<MainBottomTab> =
    MainBottomTab.values()
        .asSequence()
        .filter {//
//            val tmp = it.tag
//            val tmp2 = exceptTag.split("Header","Display","Fragment")[0]
//            Log.d("tmp","${tmp}, ${exceptTag}")
//            tmp != tmp2

            it.tag != exceptTag

        }
