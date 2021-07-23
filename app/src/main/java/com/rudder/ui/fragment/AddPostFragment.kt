package com.rudder.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rudder.R

class AddPostFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val addPost = inflater.inflate(R.layout.fragment_add_post, container, false)
        childFragmentManager.beginTransaction()
            .add(R.id.addPostHeader,AddPostHeaderFragment())
            .commit()
        return addPost
    }
}