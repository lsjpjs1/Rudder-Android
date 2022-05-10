package com.rudder.ui.fragment.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudder.R
import com.rudder.data.remote.JobsEnum
import com.rudder.databinding.FragmentJobsSavedBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsSavedAdapter
import com.rudder.util.JobsContentOnclickListener
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.*
import kotlinx.android.synthetic.main.jobs_item.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class JobsSavedFragment : Fragment(), JobsContentOnclickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentJobsSavedBinding? = null
    private val binding get() = _binding!!

    private val lazyContext by lazy {
        requireContext()
    }

    private val jobsSavedAdapter: JobsSavedAdapter by lazy {
        JobsSavedAdapter(this)
    }

    private val jobsViewModel: JobsViewModel by activityViewModels()

    //private val jobsViewModel: JobsViewModel by viewModels()
    //private lateinit var jobsViewModel: JobsViewModel
    private val purpleRudder by lazy { ContextCompat.getColor(lazyContext!!, R.color.purple_rudder) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJobsSavedBinding.inflate(layoutInflater)
        //jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
        binding.jobVM = jobsViewModel


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.jobsSavedBackButton.setOnClickListener { view ->
            val navController = view.findNavController()
            navController.popBackStack()
            (activity as MainActivity).mainBottomNavigationAppear()
        }

        jobsViewModel.getJobsMyFavorite(false)


        jobsViewModel.jobsMyFavoriteArrayList.observe(viewLifecycleOwner, Observer {
//            if (!it) {
//                Log.d("test55566", "${jobsViewModel.jobsMyFavoriteArrayList.value?.size}")
//                jobsSavedAdapter.submitList(jobsViewModel.jobsMyFavoriteArrayList.value?.toMutableList())
//                view.jobsSavedMainSwipeRefreshLayout.isRefreshing = false
//            }
            jobsSavedAdapter.submitList(it?.toMutableList())
            view.jobsSavedMainSwipeRefreshLayout.isRefreshing = false
        })


        view.jobsSavedRecyclerView.also {
            it.layoutManager = LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(false)
            it.adapter = jobsSavedAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1)){
                        jobsViewModel.scrollTouchBottomJobSaved()
                    } else if (!it.canScrollVertically(-1) && dy < 0) {

                    }
                }
            })
        }

        view.jobsSavedMainSwipeRefreshLayout.setColorSchemeColors(purpleRudder)
        view.jobsSavedMainSwipeRefreshLayout.setOnRefreshListener {
            jobsViewModel.scrollTouchTopJobSaved()
        }



    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JobsSavedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val TAG = "JobsSavedFragment"
    }

    override fun onClickContainerView(view: View, position: Int, viewTag : String) {
        jobsViewModel.getJobsDetail(viewTag.toInt(), JobsEnum.SAVED)

        val navController = findNavController()
        jobsViewModel.isJobDetailSavedResultFail.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (!it) {
                    val action = JobsSavedFragmentDirections.actionNavigationJobsSavedToNavigationJobsDetails()
                    navController.navigate(action)
                    (activity as MainActivity).mainBottomNavigationDisappear()
                }
            }
        })

    }

    override fun onClickImageView(view: View, position: Int) {

        val heartTag = view.jobsItemsHeart.getTag(R.id.borderTag)
        val jobIdTag = view.jobsItemsHeart.getTag(R.id.jobIdTag)

        if (heartTag == "not border") {
            jobsSavedAdapter.removeItem(position = position)
            jobsViewModel.changeJobsInfoFavoriteFalse(jobIdTag.toString().toInt())

            jobsViewModel.clickUnFavorite(jobIdTag.toString().toInt())
            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            view.jobsItemsHeart.setTag(R.id.borderTag, "border")

        }
    }


    override fun onStart() {
        jobsViewModel.clearJobMyFavoriteArrayList()
        super.onStart()
    }




}