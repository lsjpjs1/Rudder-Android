package com.rudder.ui.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.data.Post
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.databinding.FragmentMainBottomBarBinding
import com.rudder.ui.adapter.PostPreviewAdapter
import kotlinx.android.synthetic.main.fragment_community_display.*
import kotlinx.android.synthetic.main.fragment_community_display.view.*
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*
import java.sql.Timestamp

class CommunityDisplayFragment: Fragment() {
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityDisplayBinding>(inflater,R.layout.fragment_community_display,container,false)
        communityDisplay.postPreviewRV.also{
            it.layoutManager=LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = PostPreviewAdapter(arrayListOf(Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3),Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3),Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3),Post(1,"abc","body","title", Timestamp.valueOf("2021-07-13 11:11:11"),1,2,3)))
        }

        return communityDisplay.root
    }
}