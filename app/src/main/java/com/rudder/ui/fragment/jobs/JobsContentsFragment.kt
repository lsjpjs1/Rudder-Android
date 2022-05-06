package com.rudder.ui.fragment.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.remote.JobsEnum
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsContentAdapter
import com.rudder.util.JobsContentOnclickListener
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_contents.view.*
import kotlinx.android.synthetic.main.jobs_item.view.*

class JobsContentsFragment : Fragment(), JobsContentOnclickListener {

    private val parentActivity by lazy {
        activity as MainActivity
    }


    private val lazyContext by lazy {
        requireContext()
    }

    companion object {
        fun newInstance() = JobsContentsFragment()
        const val TAG = "JobsContentsFragment"
    }

    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }
    private val jobsContentAdapter by lazy {
        JobsContentAdapter(this)
    }

    private val jobsViewModel: JobsViewModel by activityViewModels()

    //private val jobsViewModel: JobsViewModel by viewModels()



    //private lateinit var jobsViewModel: JobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val jobsContentsDataBinding = DataBindingUtil.inflate<FragmentJobsContentsBinding>(inflater,R.layout.fragment_jobs_contents,container,false)
        //jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        jobsContentsDataBinding.jobVM = jobsViewModel

        jobsViewModel.getJobsInfo(false)

        return jobsContentsDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobsViewModel.isJobContentApiResultFail.observe(viewLifecycleOwner, Observer {
            if (!it) {
                jobsContentAdapter.submitList(jobsViewModel.jobsInfoArrayList.value!!.toMutableList())
                view.jobsContentsSwipeRefreshLayout.isRefreshing = false
            }
        })

        view.jobsContentRecyclerView.also{
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


        view.jobsContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        view.jobsContentsSwipeRefreshLayout.setOnRefreshListener {
            jobsViewModel.scrollTouchTopJobContent()

        }

        view.jobsContentHeart.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_jobs_to_navigation_jobs_saved)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }

        view.jobsContentFilter.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_jobs_to_navigation_jobs_search)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }

    }

    override fun onClickContainerView(view: View, position: Int, viewTag : String) {
        jobsViewModel.getJobsDetail(viewTag.toInt(),JobsEnum.CONTENT)

        jobsViewModel.isJobDetailContentResultFail.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (!it) {
                    val action = JobsContentsFragmentDirections.actionNavigationJobsToNavigationJobsDetails()
                    findNavController().navigate(action)
                    (activity as MainActivity).mainBottomNavigationDisappear()
                }
            }

        })

//        jobsViewModel.jobsDetailInfo.observe(viewLifecycleOwner, Observer {
//            val navController = findNavController()
//            //Log.d("test555", "content")
//                if (navController.currentDestination?.label == "Jobs") {
//                    val action = JobsContentsFragmentDirections.actionNavigationJobsToNavigationJobsDetails()
//                    val mHandler = Handler(Looper.getMainLooper())
//                    mHandler.postDelayed({
//                        navController.navigate(action)
//                    }, 1000) //
//
//
//                    //navController.navigate(action)
//                    (activity as MainActivity).mainBottomNavigationDisappear()
//
//            }
//
//        })





    }

    override fun onClickImageView(view: View, position: Int) {
        val heartTag = view.jobsItemsHeart.getTag(R.id.borderTag)
        val jobIdTag = view.jobsItemsHeart.getTag(R.id.jobIdTag)

        if (heartTag == "border") {
            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            view.jobsItemsHeart.tag = "not border"

        } else if (heartTag == "not border") {
            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            view.jobsItemsHeart.tag = "border"
        }

        jobsViewModel.clickFavorite(jobIdTag.toString().toInt())
        jobsContentAdapter.submitList(jobsViewModel.jobsInfoArrayList.value!!.toMutableList())

    }

}