package com.rudder.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.BuildConfig
import com.rudder.data.dto.JobsDetail
import com.rudder.data.dto.JobsInfo
import com.rudder.data.remote.BlockUserRequest
import com.rudder.data.remote.JobsInfoApi
import com.rudder.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class JobsViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val repository = Repository()

    var _jobsInfoArrayList = MutableLiveData<ArrayList<JobsInfo>>()
    var jobsInfoArrayList: LiveData<ArrayList<JobsInfo>> = _jobsInfoArrayList

    val _jobsDetailInfo = MutableLiveData<JobsDetail>()
    val jobsDetailInfo : LiveData<JobsDetail> = _jobsDetailInfo



    init {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>(
            JobsInfo(jobTitle = "AIRPORT SERVICES AGENT", isSaved = true,
                jobPostId = 100,
                companyName = "LINE",
                jobType = "InternShip",
                salary = "£33，500 a year",
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = "http://books.google.com/books/content?id=puFBDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"),
            JobsInfo(jobTitle = "Customer Resolution Officer", isSaved = true,
                jobPostId = 101,
                companyName = "Naver",
                jobType = "Full-Time",
                salary = "£11.22 an hour",
                postDate = Timestamp.valueOf("2021-04-13 11:11:11"),
                companyImage = null),
            JobsInfo(jobTitle = "Customer Services Advisor - Work From Home ", isSaved = true,
                jobPostId = 102,
                companyName = "Kakao",
                jobType = "Part-Time",
                salary = "£11.22 an hour",
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = "https://avatars.githubusercontent.com/u/59787852?v=4"),
            JobsInfo(jobTitle = "Early In Career Engineer Programme - IT Graduate June & September 2022", isSaved = true,
                jobPostId = 103,
                companyName = "Origin Housing",
                jobType = "Part-Time",
                salary = "£20，000-£25，000",
                postDate = Timestamp.valueOf("2020-07-13 11:11:11"),
                companyImage = "https://avatars.githubusercontent.com/u/59787852?v=4"),
            JobsInfo(jobTitle = "Graduate Technology - Technology Consulting London Autumn 2022", isSaved = false,
                jobPostId = 104,
                companyName = "The Emirates Group",
                jobType = "InternShip",
                salary = null,
                postDate = Timestamp.valueOf("2021-05-13 11:11:11"),
                companyImage = null),
            JobsInfo(jobTitle = "GOODSTART SCHEME - ENTRY LEVEL ROLES IN MEDIA PLANNING &...", isSaved = true,
                jobPostId = 105,
                companyName = "SAMSUNG",
                jobType = "InternShip",
                salary = null,
                postDate = Timestamp.valueOf("2021-07-13 11:11:11"),
                companyImage = null),

            )

        _jobsDetailInfo.value = JobsDetail(jobTitle = "Global Banking",
            jobPostId = 1000,
            companyName = "Naver",
            jobType = "Intern",
            salary = "£20，000-£25，000",
            jobLocation = "London",
            jobPostURL = "https://www.naver.com",
            postDate = Timestamp.valueOf("2021-05-15 11:11:11"),
            dueDate = Timestamp.valueOf("2020-07-17 11:11:11"),
            jobDescription = "HSBC Holdings plc is a British multinational investment bank and financial services holding company. It is the second largest bank in Europe behind BNP Paribas,[6] with total equity of US\$206.777 billion and assets of US\$2.958 trillion as of December 2021. In 2021, HSBC had \$10.8 trillion in assets under custody (AUC) and \$4.9 trillion in assets under administration (AUA), respectively.[4] HSBC traces its origin to a hong in British Hong Kong, and its present form was established in London by the Hongkong and Shanghai Banking Corporation to act as a new group holding company in 1991;[7][8] its name derives from that company's initials.[9] The Hongkong and Shanghai Banking Corporation opened branches in Shanghai in 1865[1] and was first formally incorporated in 1866.",
            isSaved = true)
    }


    fun getJobsInfo() {
        val service = JobsInfoApi.instance.jobsInfoService

        CoroutineScope(Dispatchers.IO).launch {
            //ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val response = service.jobsInfoApiFun(BlockUserRequest(token = tokenKey, blockUserInfoId = 9999))

            withContext(Dispatchers.Main) {

            }

        }

    }


}