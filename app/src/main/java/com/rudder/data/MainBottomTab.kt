package com.rudder.data

import com.rudder.R
import com.rudder.ui.fragment.community.CommunityFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment


enum class MainBottomTab(
    val itemId: Int,
    val tag: String
) {
    COMMUNITY(R.id.navigation_community, CommunityFragment.TAG),
    POSTMESSAGE(R.id.navigation_postmessage, PostMessageDisplayFragment.TAG),
    MYPAGE(R.id.navigation_mypage, MyPageDisplayFragment.TAG);

    companion object {
        fun from(itemId: Int): MainBottomTab? = values().firstOrNull { it.itemId == itemId }
    }
}

fun MainBottomTab.Companion.otherTab(exceptTag: String): Sequence<MainBottomTab> =
    MainBottomTab.values()
        .asSequence()
        .filter {
            it.tag != exceptTag
        }
