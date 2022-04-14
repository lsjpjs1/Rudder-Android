package com.rudder.ui.fragment.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.databinding.FragmentJobsDetailsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.JobsViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class JobsDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var jobsViewModel: JobsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val jobsDetailsDataBinding = DataBindingUtil.inflate<FragmentJobsDetailsBinding>(inflater,R.layout.fragment_jobs_details,container,false)
        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        jobsDetailsDataBinding.jobVM = jobsViewModel

        jobsDetailsDataBinding.jobsDetailBackButton.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()

        }

        return jobsDetailsDataBinding.root
    }

    companion object {
        const val TAG = "JobsDetailsFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment JobsDetailsFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JobsDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}