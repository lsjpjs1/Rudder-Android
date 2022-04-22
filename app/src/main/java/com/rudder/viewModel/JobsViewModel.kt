package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.BuildConfig
import com.rudder.data.dto.JobsInfo
import com.rudder.data.repository.Repository

class JobsViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val repository = Repository()

    var _jobsInfoArrayList = MutableLiveData<ArrayList<JobsInfo>>()

    var jobsInfoArrayList: LiveData<ArrayList<JobsInfo>> = _jobsInfoArrayList



    init {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>(
            JobsInfo(jobTitle = "title_0", isSaved = true,
                jobPostId = 0,
                companyName = "",
                jobType = "InternShip",
                salary = 0,
                postDate = null,
                companyImage = null),
            JobsInfo(jobTitle = "title_0", isSaved = true,
                jobPostId = 1,
                companyName = "",
                jobType = "Full-Time",
                salary = 0,
                postDate = null,
                companyImage = null),
            JobsInfo(jobTitle = "title_0", isSaved = true,
                jobPostId = 2,
                companyName = "",
                jobType = "Part-Time",
                salary = 0,
                postDate = null,
                companyImage = null),
            JobsInfo(jobTitle = "title_0", isSaved = true,
                jobPostId = 3,
                companyName = "",
                jobType = "Part-Time",
                salary = 0,
                postDate = null,
                companyImage = null),
            JobsInfo(jobTitle = "title_0", isSaved = false,
                jobPostId = 4,
                companyName = "InternShip",
                jobType = "",
                salary = 0,
                postDate = null,
                companyImage = null),
            JobsInfo(jobTitle = "title_0", isSaved = true,
                jobPostId = 5,
                companyName = "",
                jobType = "InternShip",
                salary = 0,
                postDate = null,
                companyImage = null),


            )

    }


}