package com.rudder.data

import android.util.Log
import com.rudder.R
import com.rudder.ui.fragment.community.CommunityFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment


enum class MainHeaderTab(
    val itemId: Int,
    val tag: String
) {
//    COMMUNITY(R.id.navigation_community, CommunityFragment.TAG),
//    POSTMESSAGE(R.id.navigation_postmessage, PostMessageDisplayFragment.TAG),
//    MYPAGE(R.id.navigation_mypage, MyPageDisplayFragment.TAG),


    COMMUNITYHEADER(R.id.navigation_community, "CommunityHeaderFragment"),
    POSTMESSAGEHEADER(R.id.navigation_postmessage, "PostMessageHeaderFragment"),
    MYPAGEHEADER(R.id.navigation_mypage, "MyPageHeaderFragment");

    companion object {
        fun from(itemId: Int): MainHeaderTab? = values().firstOrNull { it.itemId == itemId }
    }
}

fun MainHeaderTab.Companion.otherTab(exceptTag: String): Sequence<MainHeaderTab> =
    MainHeaderTab.values()
        .asSequence()
        .filter {
            it.tag != exceptTag
//            val tmp = it.tag.split("Header","Display","Fragment")[0]
//            val tmp2 = exceptTag.split("Header","Display","Fragment")[0]
//            Log.d("tmp","${tmp}, ${exceptTag}, ${tmp2}")
//            tmp != tmp2

        }
