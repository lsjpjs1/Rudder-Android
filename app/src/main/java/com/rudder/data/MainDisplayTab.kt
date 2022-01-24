package com.rudder.data

import android.util.Log
import com.rudder.R
import com.rudder.ui.fragment.community.CommunityDisplayFragment
import com.rudder.ui.fragment.mypage.MyPageDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageDisplayFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragment


enum class MainDisplayTab( // Just, show, hide
    val itemId: Int,
    val tag: String,
    val tabNum : Int
) {
    COMMUNITY(R.id.navigation_community, CommunityDisplayFragment.TAG, 0 ),
    POSTMESSAGE(R.id.navigation_postmessage, PostMessageDisplayFragment.TAG, 1),
    MYPAGE(R.id.navigation_mypage, MyPageDisplayFragment.TAG,2),

    SEARCH(R.id.navigation_search, SearchPostDisplayFragment.TAG, 3);


    companion object {
        fun from(itemId: Int): MainDisplayTab? = values().firstOrNull { it.itemId == itemId }
    }
}

//enum class MainAddTab( // always add, onCreateView 호출
//    val itemId: Int,
//    val tag: String,
//    val tabNum : Int
//
//) {
//    SEARCH(R.id.navigation_search, SearchPostDisplayFragment.TAG, 3);
//}

object MainAddTab { // always add, onCreateView 호출
    val addTabFragmentTagList = arrayListOf<String>(SearchPostDisplayFragment.TAG)
}



fun MainDisplayTab.Companion.otherTab(exceptTag: String): Sequence<MainDisplayTab> =
    MainDisplayTab.values()
        .asSequence()
        .filter {//
            it.tag != exceptTag
        }
