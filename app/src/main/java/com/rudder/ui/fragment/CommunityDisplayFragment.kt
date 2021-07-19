package com.rudder.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentCommunityDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.PostPreviewAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.MainViewModel

class CommunityDisplayFragment: Fragment(),CustomOnclickListener {
    private val viewModel = MainViewModel
    private val lazyContext by lazy {
        requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val communityDisplay = DataBindingUtil.inflate<FragmentCommunityDisplayBinding>(inflater,R.layout.fragment_community_display,container,false)
        val adapter = PostPreviewAdapter(viewModel.posts.value!!,this,lazyContext)
        communityDisplay.postPreviewRV.also{
            it.layoutManager=LinearLayoutManager(lazyContext)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.updatePosts(it)
        })



        return communityDisplay.root
    }

    override fun onPostPreviewClick(view: View, position: Int) {
        showPost()
    }

    fun showPost(){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.hide(CommunityFragment())
        transaction.add(R.id.mainDisplay,ShowPostFragment())
        transaction.show(ShowPostFragment())
        transaction.commit()
    }

}