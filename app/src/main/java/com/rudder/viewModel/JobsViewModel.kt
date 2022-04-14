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
            JobsInfo(jobTitle = "title_0"),
            JobsInfo(jobTitle = "title_1"),
            JobsInfo(jobTitle = "title_2"),
            JobsInfo(jobTitle = "title_3"),
            JobsInfo(jobTitle = "title_4"),
            JobsInfo(jobTitle = "title_5"),
            JobsInfo(jobTitle = "title_6"),
            JobsInfo(jobTitle = "title_7"),
            JobsInfo(jobTitle = "title_8"),
            JobsInfo(jobTitle = "title_9"),
            JobsInfo(jobTitle = "title_10"),
            JobsInfo(jobTitle = "title_11")
            )

    }


}