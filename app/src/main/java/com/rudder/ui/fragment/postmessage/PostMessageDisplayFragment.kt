package com.rudder.ui.fragment.postmessage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.ui.adapter.PostMessageAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.PostMessageViewModel

class PostMessageDisplayFragment : Fragment() {

    private val viewModel: PostMessageViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }

    companion object {
        const val TAG = "PostMessageDisplayFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = DataBindingUtil.inflate<FragmentPostMessageDisplayBinding>(inflater,
            R.layout.fragment_post_message_display, container, false)

        fragmentBinding.lifecycleOwner = this

        childFragmentManager.beginTransaction()
            .add(R.id.postMessageHeader, PostMessageHeaderFragment())
            .commit()

        val adapter = PostMessageAdapter(activity as MainActivityInterface)

        viewModel.getPostMessages()

        fragmentBinding.postMessageDisplayRV.also {
            it.layoutManager =
                LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        viewModel.myMessageRooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })



        return fragmentBinding.root
    }




}