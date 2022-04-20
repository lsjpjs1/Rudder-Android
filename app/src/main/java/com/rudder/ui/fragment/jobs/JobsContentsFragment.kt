package com.rudder.ui.fragment.jobs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsContentAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.util.JobsContentOnclickListener
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_contents.view.*
import kotlinx.android.synthetic.main.jobs_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


    private lateinit var jobsViewModel: JobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val jobsContentsDataBinding = DataBindingUtil.inflate<FragmentJobsContentsBinding>(inflater,R.layout.fragment_jobs_contents,container,false)
        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        jobsContentsDataBinding.jobVM = jobsViewModel


//        jobsContentsDataBinding.JobsMainRecyclerView.also{
//            it.layoutManager = LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false)
//            it.setHasFixedSize(false)
//            it.adapter = JobsContentAdapter(jobsViewModel.jobsInfoArrayList.value!!)
//        }

        return jobsContentsDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.jobsContentRecyclerView.also{
            it.layoutManager = LinearLayoutManager(parentActivity, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = JobsContentAdapter(jobsViewModel.jobsInfoArrayList.value!!, this)
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        //mainViewModel.scrollTouchBottomCommunityPost()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {

                    }
                }
            })
        }


        view.jobsContentsSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        view.jobsContentsSwipeRefreshLayout.setOnRefreshListener {
            //mainViewModel.scrollTouchTopCommunityPost()
        }

        view.jobsContentHeart.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_jobs_to_navigation_jobs_saved)
            (activity as MainActivity).mainBottomNavigationDisappear()
        }

    }

    override fun onClickContainerView(view: View, position: Int) {
        Toast.makeText(parentActivity, "title ${position}", Toast.LENGTH_SHORT).show()


        val action = JobsContentsFragmentDirections.actionNavigationJobsToNavigationJobsDetails()
        view.findNavController().navigate(action)
        (activity as MainActivity).mainBottomNavigationDisappear()

    }

    override fun onClickImageView(view: View, position: Int) {
        val heartTag = view.jobsItemsHeart.tag

        if (heartTag == "border") {
            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            view.jobsItemsHeart.tag = "not border"

        } else if (heartTag == "not border") {
            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            view.jobsItemsHeart.tag = "border"
        }

    }

}