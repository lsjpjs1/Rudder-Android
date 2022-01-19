package com.rudder.data

import com.rudder.R
import com.rudder.ui.fragment.community.CommunityFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment


enum class MainBottomTab(
    val itemId: Int,
    val tag: String
) {
    APPLE(R.id.navigation_apple, "AppleFragment"),
    BANANA(R.id.navigation_banana, "BananaFragment"),
    CECECE(R.id.navigation_cecece, "CececeFragment");

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
