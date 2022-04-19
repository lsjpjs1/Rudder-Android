package com.rudder.ui.fragment.jobs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.databinding.FragmentJobsDetailsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_details.view.*
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.*
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.jobsSavedBackButton

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val jobsDetailsDataBinding = DataBindingUtil.inflate<FragmentJobsDetailsBinding>(inflater,R.layout.fragment_jobs_details,container,false)
        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        jobsDetailsDataBinding.jobVM = jobsViewModel


        return jobsDetailsDataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.jobsDetailBackButton.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()

        }



        view.jobsDetailDiscussionBoardTV.setOnClickListener { view ->
            val action = JobsDetailsFragmentDirections.actionNavigationJobsSavedToNavigationCommunity()
            view.findNavController().navigate(action)
            (activity as MainActivity).mainBottomNavigationAppear()

        }
    }


}
