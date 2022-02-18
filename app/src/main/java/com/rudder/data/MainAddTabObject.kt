package com.rudder.data

import com.rudder.ui.fragment.mypage.CategorySelectMyPageFragment
import com.rudder.ui.fragment.mypage.MyPostDisplayFragment
import com.rudder.ui.fragment.post.AddPostDisplayFragment
import com.rudder.ui.fragment.post.EditPostFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.ui.fragment.postmessage.PostMessageRoomFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragment

object MainAddTabObject {
    val addTabFragmentTagList = arrayListOf<String>(
        SearchPostDisplayFragment.TAG,
        AddPostDisplayFragment.TAG,
        ShowPostDisplayFragment.TAG,
        CategorySelectMyPageFragment.TAG,
        EditPostFragment.TAG,
        PostMessageRoomFragment.TAG,
        MyPostDisplayFragment.TAG
    )
}