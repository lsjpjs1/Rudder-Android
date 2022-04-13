package com.rudder.ui.fragment.jobs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.databinding.FragmentCommunityContentsBinding
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsAdapter
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_commuinty_selector.view.*
import kotlinx.android.synthetic.main.fragment_jobs_contents.view.*

class JobsContentsFragment : Fragment() {

    private val parentActivity by lazy {
        activity as MainActivity
    }

    companion object {
        fun newInstance() = JobsContentsFragment()
        const val TAG = "JobsContentsFragment"
    }

    private lateinit var jobsViewModel: JobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val jobsContentsDataBinding = DataBindingUtil.inflate<FragmentJobsContentsBinding>(inflater,R.layout.fragment_jobs_contents,container,false)

        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)


//        jobsContentsDataBinding.JobsMainRecyclerView.also{
//            it.layoutManager = LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false)
//            it.setHasFixedSize(false)
//            it.adapter = JobsAdapter(jobsViewModel.jobsInfoArrayList.value!!)
//        }

        return jobsContentsDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.JobsMainRecyclerView.also{
            it.layoutManager = LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = JobsAdapter(jobsViewModel.jobsInfoArrayList.value!!)
        }

    }


}