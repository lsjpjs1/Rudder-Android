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
            JobsInfo(JobsTitle = "title_1"),
            JobsInfo(JobsTitle = "title_2"),
            JobsInfo(JobsTitle = "title_3"),
            JobsInfo(JobsTitle = "title_4"),
            JobsInfo(JobsTitle = "title_5"),
            JobsInfo(JobsTitle = "title_6"),
            JobsInfo(JobsTitle = "title_7"),
            JobsInfo(JobsTitle = "title_8"),
        )

    }


}