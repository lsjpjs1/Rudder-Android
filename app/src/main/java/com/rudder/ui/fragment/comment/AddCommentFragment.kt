package com.rudder.ui.fragment.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentAddCommentBinding
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_comment.view.*

class AddCommentFragment() : Fragment() {
    private lateinit var addComment: FragmentAddCommentBinding
    private val viewModel by lazy {
        (parentFragment as ShowPostDisplayFragment).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addComment = DataBindingUtil.inflate<FragmentAddCommentBinding>(inflater, R.layout.fragment_add_comment,container,false)
        viewModel.commentBodyClear()

        addComment.mainVM = viewModel
        addComment.lifecycleOwner = this


        viewModel.commentBody.observe(viewLifecycleOwner, Observer {

            it?.let {
                addComment.root.replyButton.isEnabled = !it.isEmpty()
            }
        })
        return addComment.root
    }
}