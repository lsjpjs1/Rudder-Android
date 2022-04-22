package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.BuildConfig
import com.rudder.data.dto.JobsInfo
import com.rudder.data.repository.Repository
import java.sql.Timestamp

class JobsViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val repository = Repository()

    var _jobsInfoArrayList = MutableLiveData<ArrayList<JobsInfo>>()

    var jobsInfoArrayList: LiveData<ArrayList<JobsInfo>> = _jobsInfoArrayList



    init {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>(
            JobsInfo(jobTitle = "AIRPORT SERVICES AGENT", isSaved = true,
                jobPostId = 0,
                companyName = "LINE",
                jobType = "InternShip",
                salary = "£33，500 a year",
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = "https://avatars.githubusercontent.com/u/59787852?v=4"),
            JobsInfo(jobTitle = "Customer Resolution Officer", isSaved = true,
                jobPostId = 1,
                companyName = "Naver",
                jobType = "Full-Time",
                salary = "£11.22 an hour",
                postDate = Timestamp.valueOf("2021-04-13 11:11:11"),
                companyImage = null),
            JobsInfo(jobTitle = "Customer Services Advisor - Work From Home ", isSaved = true,
                jobPostId = 2,
                companyName = "Kakao",
                jobType = "Part-Time",
                salary = "£11.22 an hour",
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = "https://avatars.githubusercontent.com/u/59787852?v=4"),
            JobsInfo(jobTitle = "Early In Career Engineer Programme - IT Graduate June & September 2022", isSaved = true,
                jobPostId = 3,
                companyName = "Origin Housing",
                jobType = "Part-Time",
                salary = "£20，000-£25，000",
                postDate = Timestamp.valueOf("2020-07-13 11:11:11"),
                companyImage = "https://avatars.githubusercontent.com/u/59787852?v=4"),
            JobsInfo(jobTitle = "Graduate Technology - Technology Consulting London Autumn 2022", isSaved = false,
                jobPostId = 4,
                companyName = "The Emirates Group",
                jobType = "InternShip",
                salary = null,
                postDate = Timestamp.valueOf("2021-05-13 11:11:11"),
                companyImage = null),
            JobsInfo(jobTitle = "GOODSTART SCHEME - ENTRY LEVEL ROLES IN MEDIA PLANNING &...", isSaved = true,
                jobPostId = 5,
                companyName = "SAMSUNG",
                jobType = "InternShip",
                salary = null,
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = null),

            )
    }


}