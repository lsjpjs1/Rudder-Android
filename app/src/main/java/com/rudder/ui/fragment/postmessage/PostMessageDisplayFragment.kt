package com.rudder.ui.fragment.postmessage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rudder.R
import com.rudder.databinding.FragmentPostMessageDisplayBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.activity.MainActivityInterface
import com.rudder.ui.adapter.PostMessageAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.util.PostMessageAdapterCallback
import com.rudder.viewModel.PostMessageViewModel

class PostMessageDisplayFragment : Fragment(),PostMessageAdapterCallback {

    private val viewModel: PostMessageViewModel by viewModels()
    private val lazyContext by lazy {
        context
    }

    companion object {
        const val TAG = "PostMessageDisplayFragment"
    }

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }

    val args : PostMessageDisplayFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentBinding = DataBindingUtil.inflate<FragmentPostMessageDisplayBinding>(inflater,
            R.layout.fragment_post_message_display, container, false)

        fragmentBinding.lifecycleOwner = this

//        childFragmentManager.beginTransaction()
//            .add(R.id.postMessageHeader, PostMessageHeaderFragment())
//            .commit()

        val adapter = PostMessageAdapter(this, lazyContext!!)

        viewModel.getPostMessages()


        fragmentBinding.postMessageDisplayRV.also {
            it.layoutManager =
                LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = adapter
        }

        fragmentBinding.postMessageDisplaySwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        fragmentBinding.postMessageDisplaySwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getPostMessages()
            }

        })

        viewModel.myMessageRooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
            fragmentBinding.postMessageDisplaySwipeRefreshLayout.isRefreshing=false
        })
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean?>("onMessageSend")?.observe(viewLifecycleOwner) {result ->
            result?.let {
                if (result) {
                    viewModel.getPostMessages()
                }
            }
        }

        Log.d("asdasd123", "${args.notificationPostMessageRoomId}")

//        val argsNotificationPostMessageId = args.notificationPostMessageRoomId
//        if (argsNotificationPostMessageId != -1){
//            onClickPostMessageRoom(argsNotificationPostMessageId)
//        }


        return fragmentBinding.root
    }


    override fun onAttach(context: Context) {
        Log.d("asdasd123","onAttach")

        super.onAttach(context)
    }

    override fun onResume() {
        Log.d("asdasd123","resume")
        super.onResume()
    }

    override fun onStart() {
        Log.d("asdasd123","start")

        super.onStart()
    }





    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            viewModel.getPostMessages()
        }

        super.onHiddenChanged(hidden)
    }

    override fun onClickPostMessageRoom(postMessageRoomId: Int) {
        val action = PostMessageDisplayFragmentDirections.actionNavigationPostmessageToNavigationPostmessageRoom(postMessageRoomId)
        findNavController().navigate(action)

        (activity as MainActivity).mainBottomNavigationDisappear()


    }


}