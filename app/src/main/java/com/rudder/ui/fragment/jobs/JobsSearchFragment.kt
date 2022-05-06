package com.rudder.ui.fragment.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.databinding.FragmentJobsSearchBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsContentAdapter
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_contents.view.*
import kotlinx.android.synthetic.main.fragment_jobs_search.view.*

class JobsSearchFragment : Fragment() {


    private val parentActivity by lazy {
        activity as MainActivity
    }


    private var _binding : FragmentJobsSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobsViewModel: JobsViewModel
    private val lazyContext by lazy {
        requireContext()
    }
    private val jobsContentAdapter by lazy {
        JobsContentAdapter(this)
    }

    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            JobsSearchFragment().apply {
//                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
//                }
//            }

        const val TAG = "JobsSearchFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentJobsSearchBinding.inflate(layoutInflater)
        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        binding.jobVM = jobsViewModel



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.jobsSearchBackButton.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }


        view.jobsSearchRecyclerView.also{
            it.layoutManager = LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = jobsContentAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        jobsViewModel.scrollTouchBottomJobInfoPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {

                    }
                }
            })
        }

    }

}