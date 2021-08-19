package com.rudder.ui.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rudder.R
import kotlinx.android.synthetic.main.fragment_main_bottom_bar.view.*

class MyPageFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val MyPage = inflater.inflate(R.layout.fragment_my_page, container, false)

        childFragmentManager.beginTransaction()
            .add(R.id.myPageHeader, MyPageHeaderFragment())
            .commit()

        return MyPage
    }
}