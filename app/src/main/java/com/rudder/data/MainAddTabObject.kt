package com.rudder.data

import com.rudder.ui.fragment.post.AddPostDisplayFragment
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.ui.fragment.search.SearchPostDisplayFragment

object MainAddTabObject {
    val addTabFragmentTagList = arrayListOf<String>(
        SearchPostDisplayFragment.TAG,
        AddPostDisplayFragment.TAG,
        ShowPostDisplayFragment.TAG
    )
}