package com.rudder.ui.fragment.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.data.remote.JobsEnum
import com.rudder.databinding.FragmentJobsSearchBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsContentAdapter
import com.rudder.util.JobsContentOnclickListener
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_search.view.*

class JobsSearchFragment : Fragment(), JobsContentOnclickListener {


    private val parentActivity by lazy {
        activity as MainActivity
    }

    private var _binding : FragmentJobsSearchBinding? = null
    private val binding get() = _binding!!
    private val lazyContext by lazy {
        requireContext()
    }
    private val jobsContentAdapter by lazy {
        JobsContentAdapter(this)
    }

    companion object {
        const val TAG = "JobsSearchFragment"
    }

    private val jobsViewModel: JobsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentJobsSearchBinding.inflate(layoutInflater)
        //jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
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
                        //jobsViewModel.scrollTouchBottomJobInfoPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {

                    }
                }
            })
        }


        jobsViewModel.isJobContentApiResultFail.observe(viewLifecycleOwner, Observer {
            if (!it) {
                jobsContentAdapter.submitList(jobsViewModel.jobsSearchArrayList.value!!.toMutableList())
                //view.jobsContentsSwipeRefreshLayout.isRefreshing = false
            }
        })


        view.jobsSearchSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    jobsViewModel.getJobsInfo(isScroll = false, searchWord = query)
                }
                view.jobsSearchSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{

                }
                return true
            }
        })

    }

    override fun onClickContainerView(view: View, position: Int, viewTag: String) {
        jobsViewModel.getJobsDetail(viewTag.toInt(),JobsEnum.SEARCH)


        val navController = findNavController()
        jobsViewModel.isJobDetailSearchResultFail.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (!it) {
                    val action = JobsSearchFragmentDirections.actionNavigationJobsSearchToNavigationJobsDetails()
                    findNavController().navigate(action)
                    (activity as MainActivity).mainBottomNavigationDisappear()
                }
            }
        })

    }

    override fun onClickImageView(view: View, position: Int) {
        TODO("Not yet implemented")
    }

}