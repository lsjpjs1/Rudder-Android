package com.rudder.ui.fragment.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentAddCommentBinding
import com.rudder.ui.fragment.post.ShowPostDisplayFragment
import kotlinx.android.synthetic.main.fragment_add_comment.view.*

class AddCommentFragment() : Fragment() {
    private lateinit var addComment: FragmentAddCommentBinding
    private val mainViewModel by lazy {
        (parentFragment as ShowPostDisplayFragment).viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addComment = DataBindingUtil.inflate<FragmentAddCommentBinding>(inflater, R.layout.fragment_add_comment,container,false)
        mainViewModel.commentBodyClear()
        addComment.mainVM = mainViewModel
        addComment.lifecycleOwner = this

        mainViewModel.commentBody.observe(viewLifecycleOwner, Observer {

            it?.let {
                addComment.root.replyButton.isEnabled = !it.isBlank()
            }
        })
        return addComment.root
    }
}