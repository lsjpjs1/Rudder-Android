package com.rudder.ui.fragment.jobs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.R
import com.rudder.databinding.FragmentAddCommentBinding
import com.rudder.databinding.FragmentJobsDetailsBinding
import com.rudder.databinding.FragmentJobsSavedBinding
import com.rudder.databinding.FragmentJobsSavedBindingImpl
import com.rudder.ui.activity.MainActivity
import com.rudder.ui.adapter.JobsContentAdapter
import com.rudder.ui.adapter.JobsSavedAdapter
import com.rudder.util.JobsContentOnclickListener
import com.rudder.viewModel.JobsViewModel
import kotlinx.android.synthetic.main.fragment_jobs_saved.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JobsSavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobsSavedFragment : Fragment(), JobsContentOnclickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentJobsSavedBinding? = null
    private val binding get() = _binding!!

    private lateinit var jobsViewModel: JobsViewModel
    private val lazyContext by lazy {
        requireContext()
    }

    private val jobsSavedAdapter: JobsSavedAdapter by lazy {
        JobsSavedAdapter(this)
    }


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
        jobsViewModel = ViewModelProvider(this).get(JobsViewModel::class.java)
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


        view.jobsSavedRecyclerView.apply {
            layoutManager = LinearLayoutManager(lazyContext, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
            adapter = jobsSavedAdapter
        }
        jobsSavedAdapter.submitList(jobsViewModel.jobsInfoArrayList.value!!)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JobsSavedFragment.
         */
        // TODO: Rename and change types and number of parameters
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

    override fun onClickContainerView(view: View, position: Int) {
        //TODO("Not yet implemented")
    }

    override fun onClickImageView(view: View, position: Int) {
        //TODO("Not yet implemented")
        jobsSavedAdapter.removeItem(position = position)
    }
}