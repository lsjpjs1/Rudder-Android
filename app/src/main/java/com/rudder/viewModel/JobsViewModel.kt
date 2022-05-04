package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.dto.JobsDetail
import com.rudder.data.dto.JobsInfo
import com.rudder.data.remote.JobsInfoApi
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
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


    private val _isApiResultFail = MutableLiveData<Boolean>()
    val isApiResultFail: LiveData<Boolean> = _isApiResultFail



    init {

        _jobsInfoArrayList.value = arrayListOf<JobsInfo>()

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
            isSaved = true
        )
    }


    fun scrollTouchTopJobContent() {
        clearJobInfo()
        getJobsInfo()
    }



    fun clearJobInfo() {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>()
    }


    fun getJobsInfo() {
        val service = JobsInfoApi.instance.jobsInfoService

        CoroutineScope(Dispatchers.IO).launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val response = service.jobsInfoApiFun(endPostId = null, searchBody = null, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")

            withContext(Dispatchers.Main) {
                if (response.code() == 200) { // 서버 통신 success
                    val result = response.body()
                    val getItems = result!!.getAsJsonArray("jobs")
                    Log.d("getJobsInfo", "${getItems}")

                    for (getItem in getItems) {
                        val jsonObject = getItem as JsonObject
                        _jobsInfoArrayList.value!!.add(
                            JobsInfo(
                                jobTitle = jsonObject.get("jobTitle").toString().drop(1).dropLast(1),
                                jobPostId = jsonObject.get("jobId").asInt,
                                companyName = jsonObject.get("companyName").toString().drop(1).dropLast(1),
                                jobType = if (jsonObject.get("jobType") != null) "" else jsonObject.get("jobType").toString().drop(1).dropLast(1),
                                salary = jsonObject.get("salary").toString().drop(1).dropLast(1),
                                postDate = Timestamp.valueOf(jsonObject.get("uploadDate").toString().split('T').joinToString(" ").drop(1).dropLast(11)),
                                companyImage = null,
                                isSaved = jsonObject.get("isFavorite").asBoolean
                            )
                        )
                    }

                    _isApiResultFail.postValue(false)

                } else { // 서버 통신 fail
                    _isApiResultFail.postValue(true)
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }

    }


}