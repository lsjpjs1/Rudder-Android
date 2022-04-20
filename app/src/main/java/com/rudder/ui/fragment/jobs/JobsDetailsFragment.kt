package com.rudder.ui.fragment.jobs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentJobsContentsBinding
import com.rudder.databinding.FragmentJobsDetailsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.CategorySelectorAdapter
import com.rudder.util.CustomOnclickListener
import com.rudder.viewModel.JobsViewModel
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.category_selector.view.*
import kotlinx.android.synthetic.main.fragment_jobs_details.view.*
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.*
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.jobsSavedBackButton
import kotlinx.android.synthetic.main.jobs_item.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class JobsDetailsFragment : Fragment(), CustomOnclickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    private val lazyContext by lazy {
        requireContext()
    }

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
            //onClickView(view, 2)
            mainViewModel.selectedCategoryView.value?.also {
                it.categoryUnderBarView.visibility = View.GONE
                it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.grey))


                it.categoryUnderBarView.visibility = View.VISIBLE
                it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.black))
            }



            mainViewModel.setSelectedCategoryPosition(2)
            mainViewModel.setSelectedCategoryView(mainViewModel.selectedCategoryView.value!!)
            mainViewModel.clearPosts()
            mainViewModel.getPosts()

        }

//        if (heartTag == "border") {
//            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
//            view.jobsItemsHeart.tag = "not border"
//
//        } else if (heartTag == "not border") {
//            view.jobsItemsHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//            view.jobsItemsHeart.tag = "border"
//        }


        view.jobsDetailHeartCL.setOnClickListener {
            if (view.jobsDetailHeart.tag == "border") {
                view.jobsDetailHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
                view.jobsDetailHeart.setColorFilter(ContextCompat.getColor(lazyContext,R.color.purple_rudder))
                view.jobsDetailHeart.tag = "not border"
                it.background = ContextCompat.getDrawable(lazyContext, R.drawable.edge)
            } else if (view.jobsDetailHeart.tag == "not border") {
                view.jobsDetailHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                view.jobsDetailHeart.setColorFilter(ContextCompat.getColor(lazyContext,R.color.grey))
                view.jobsDetailHeart.tag = "border"
                it.background = ContextCompat.getDrawable(lazyContext, R.drawable.edge_grey)
            }

        }


        view.jobsDetailApplyButton.setOnClickListener {
            val url = "https://www.naver.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }

    }

    override fun onClickView(view: View, position: Int) {
        mainViewModel.selectedCategoryView.value?.also {
            it.categoryUnderBarView.visibility = View.GONE
            it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.grey))
        }
        mainViewModel.setSelectedCategoryPosition(position)
        mainViewModel.setSelectedCategoryView(view)
        mainViewModel.clearPosts()
        mainViewModel.getPosts()
    }


}
