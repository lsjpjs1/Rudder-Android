package com.rudder.ui.fragment.jobs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.rudder.R
import com.rudder.databinding.FragmentJobsDetailsBinding
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.CategorySelectorAdapter
import com.rudder.viewModel.JobsViewModel
import com.rudder.viewModel.MainViewModel
import kotlinx.android.synthetic.main.category_selector.view.*
import kotlinx.android.synthetic.main.fragment_commuinty_selector.*
import kotlinx.android.synthetic.main.fragment_jobs_details.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class JobsDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val mainViewModel: MainViewModel by activityViewModels()


    private val lazyContext by lazy {
        requireContext()
    }

    private val parentActivity by lazy {
        activity as MainActivity
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



        jobsDetailsDataBinding.jobsDetailCompanyTV.text = jobsViewModel.jobsDetailInfo.value!!.companyName
        jobsDetailsDataBinding.jobsDetailLocationTV.text = jobsViewModel.jobsDetailInfo.value!!.jobLocation
        jobsDetailsDataBinding.jobsDetailSalaryTV.text = jobsViewModel.jobsDetailInfo.value!!.salary
        jobsDetailsDataBinding.jobsDetailTypeTV.text = jobsViewModel.jobsDetailInfo.value!!.jobType
        jobsDetailsDataBinding.jobsDetailFullJobDescriptionTV.text = jobsViewModel.jobsDetailInfo.value!!.jobDescription
        jobsDetailsDataBinding.jobsDetailPostTimeTV.text = jobsViewModel.jobsDetailInfo.value!!.postDate.toString()
        jobsDetailsDataBinding.jobsDetailDeadlineTV.text = jobsViewModel.jobsDetailInfo.value!!.dueDate.toString()

        if (jobsViewModel.jobsDetailInfo.value!!.isSaved) {
            jobsDetailsDataBinding.jobsDetailHeart.setImageResource(R.drawable.ic_baseline_favorite_24)
            jobsDetailsDataBinding.jobsDetailHeart.setColorFilter(ContextCompat.getColor(lazyContext,R.color.purple_rudder))
            jobsDetailsDataBinding.jobsDetailHeart.tag = "not border"
            jobsDetailsDataBinding.jobsDetailHeartCL.background = ContextCompat.getDrawable(lazyContext, R.drawable.edge)

        } else {
            jobsDetailsDataBinding.jobsDetailHeart.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            jobsDetailsDataBinding.jobsDetailHeart.setColorFilter(ContextCompat.getColor(lazyContext,R.color.grey))
            jobsDetailsDataBinding.jobsDetailHeart.tag = "border"
            jobsDetailsDataBinding.jobsDetailHeartCL.background = ContextCompat.getDrawable(lazyContext, R.drawable.edge_grey)

        }


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

            val jobPosition = mainViewModel.userSelectCategories.value!!.map{it.categoryName}.indexOf("five")
            moveToJobCategoryPreviewPost(jobPosition)

        }

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
            val url = jobsViewModel.jobsDetailInfo.value!!.jobPostURL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


        view.jobsDetailShareCL.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

    fun moveToJobCategoryPreviewPost(findJobIndex : Int){
        var viewPosition = findJobIndex
        mainViewModel.selectedCategoryView.value?.also {
            it.categoryUnderBarView.visibility = View.GONE
            it.categoryTextView.setTextColor(ContextCompat.getColor(lazyContext,R.color.grey))
        }
        if (findJobIndex == -1) { // 내가 고른 카테고리중 job이 아예 없으면, ALL로 감.
            viewPosition = 0
        }
        val viewHolder = parentActivity.categoryRecyclerView.findViewHolderForAdapterPosition(viewPosition)
        val categorySelectorConstraintLayoutView = (viewHolder as CategorySelectorAdapter.CustomViewHolder).categorySelectorBinding.categorySelectorConstraintLayout
        mainViewModel.setSelectedCategoryPosition(viewPosition)
        mainViewModel.setSelectedCategoryView(categorySelectorConstraintLayoutView)
        mainViewModel.clearPosts()
        mainViewModel.getPosts()
    }


}
