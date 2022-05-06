package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.rudder.BuildConfig
import com.rudder.data.dto.JobsDetail
import com.rudder.data.dto.JobsInfo
import com.rudder.data.remote.JobsEnum
import com.rudder.data.remote.JobsInfoApi
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.sql.Timestamp

class JobsViewModel : ViewModel() {
    private val tokenKey = BuildConfig.TOKEN_KEY
    private val repository = Repository()

    private val _searchWord = MutableLiveData<String?>()
    val searchWord: LiveData<String?> = _searchWord

    var _jobsInfoArrayList = MutableLiveData<ArrayList<JobsInfo>>()
    var jobsInfoArrayList: LiveData<ArrayList<JobsInfo>> = _jobsInfoArrayList

    var _jobsSearchArrayList = MutableLiveData<ArrayList<JobsInfo>>()
    var jobsSearchArrayList: LiveData<ArrayList<JobsInfo>> = _jobsSearchArrayList

    var _jobsMyFavoriteArrayList = MutableLiveData<ArrayList<JobsInfo>>()
    var jobsMyFavoriteArrayList: LiveData<ArrayList<JobsInfo>> = _jobsMyFavoriteArrayList

    val _jobsDetailInfo = MutableLiveData<JobsDetail>()
    val jobsDetailInfo : LiveData<JobsDetail> = _jobsDetailInfo


    private val _isJobContentApiResultFail = MutableLiveData<Boolean>()
    val isJobContentApiResultFail: LiveData<Boolean> = _isJobContentApiResultFail

    private val _isJobDetailApiResultFail = MutableLiveData<Event<Boolean>>()
    val isJobDetailApiResultFail: LiveData<Event<Boolean>> = _isJobDetailApiResultFail


    private val _isJobDetailContentResultFail = MutableLiveData<Event<Boolean>>()
    val isJobDetailContentResultFail: LiveData<Event<Boolean>> = _isJobDetailContentResultFail

    private val _isJobDetailSavedResultFail = MutableLiveData<Event<Boolean>>()
    val isJobDetailSavedResultFail: LiveData<Event<Boolean>> = _isJobDetailSavedResultFail

    private val _isJobDetailSearchResultFail = MutableLiveData<Event<Boolean>>()
    val isJobDetailSearchResultFail: LiveData<Event<Boolean>> = _isJobDetailSearchResultFail


    private val _isJobMyFavoriteApiResultFail = MutableLiveData<Boolean>()
    val isJobMyFavoriteApiResultFail: LiveData<Boolean> = _isJobMyFavoriteApiResultFail


    var pagingIndex = 0
    var endContentJobsId = -1
    var endSearchJobsId = -1



    init {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>()
        _jobsSearchArrayList.value = arrayListOf<JobsInfo>()
        _jobsMyFavoriteArrayList.value = arrayListOf<JobsInfo>()
        _searchWord.value = null
        _jobsDetailInfo.value = JobsDetail(jobTitle = null,
            jobId = null,
            companyName = null,
            jobType = null,
            salary = null,
            location = null,
            jobUrl = null,
            uploadDate = Timestamp.valueOf("2021-07-13 11:11:11"),
            expireDate = null,
            jobDescription = null,
            isFavorite = false)
    }


    fun scrollTouchBottomJobInfoPost() {
        if (_jobsInfoArrayList.value!!.size > 0) {
            pagingIndex += 1
            endContentJobsId = _jobsInfoArrayList.value!![_jobsInfoArrayList.value!!.size - 1].jobPostId
            getJobsInfo(true)
        }
    }



    fun scrollTouchTopJobContent() {
        clearJobInfo()
        getJobsInfo(false)
    }


    fun scrollTouchTopJobSearch() {
        clearJobSearch()
        getJobsInfo(false)
    }

    fun clearJobSearch() {
        _jobsSearchArrayList.value = arrayListOf<JobsInfo>()
        pagingIndex = 0
        endSearchJobsId = -1
    }


    fun clearJobInfo() {
        _jobsInfoArrayList.value = arrayListOf<JobsInfo>()
        pagingIndex = 0
        endContentJobsId = -1
    }

    fun getJobsInfo(isScroll: Boolean, searchWord : String? = null) {
        val service = JobsInfoApi.instance.jobsInfoService

        CoroutineScope(Dispatchers.IO).launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val response : Response<JsonObject>
            if (isScroll) {
                response = service.jobsInfoApiFun(endPostId = endContentJobsId, searchBody = _searchWord.value, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            } else {
                response = service.jobsInfoApiFun(endPostId = null, searchBody = _searchWord.value, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            }

            withContext(Dispatchers.Main) {
                if (response.code() == 200) { // 서버 통신 success
                    val result = response.body()
                    val getItems = result!!.getAsJsonArray("jobs")
                    //Log.d("getJobsInfo", "${getItems}")

                    for (getItem in getItems) {
                        val jsonObject = getItem as JsonObject

                        if (_searchWord.value == null) { // search가 아닌 content인 경우
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
                        } else { // search 인 경우
                            _jobsSearchArrayList.value!!.add(
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

                    }
                    _isJobContentApiResultFail.postValue(false)
                } else { // 서버 통신 fail
                    _isJobContentApiResultFail.postValue(true)
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }



    fun getJobsDetail(jobId : Int, whereCall : JobsEnum) {
        val service = JobsInfoApi.instance.jobsInfoService
       CoroutineScope(Dispatchers.IO).launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val response = service.jobsByJobIdApiFun(jobId = jobId, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            withContext(Dispatchers.Main) {
                if (response.code() == 200) { // 서버 통신 success
                    val result = response.body()
                    Log.d("test123", "${result}")
                    _jobsDetailInfo.value = result!!
                    when (whereCall) {
                        JobsEnum.CONTENT -> {
                            _isJobDetailContentResultFail.postValue(Event(false))
                        }
                        JobsEnum.SAVED -> {
                            _isJobDetailSavedResultFail.postValue(Event(false))
                        }
                        JobsEnum.SEARCH -> {
                            _isJobDetailSearchResultFail.postValue(Event(false))
                        }
                    }
                } else { // 서버 통신 fail
                    _isJobDetailApiResultFail.postValue(Event(true))
                }
            }
           ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun clickFavorite(jobId : Int) {
        val service = JobsInfoApi.instance.jobsInfoService

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.jobsFavoriteClickApiFun(jobId = jobId, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            withContext(Dispatchers.Main) {
                if (response.code() == 201) {
                    val result = response.body()
                    Log.d("clickFavorite", "$result")
                    val jsonObject = result as JsonObject
                    val tmp = jsonObject.get("jobId")

                }
            }
        }
    }


    fun getJobsMyFavorite(isScroll: Boolean) {
        val service = JobsInfoApi.instance.jobsInfoService

        CoroutineScope(Dispatchers.IO).launch {
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))
            val response : Response<JsonObject>
            if (isScroll) {
                response = service.jobsGetMyFavoriteApiFun(endPostId = endContentJobsId, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            } else {
                response = service.jobsGetMyFavoriteApiFun(endPostId = null, token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjoiUk9MRV9VU0VSIiwic2Nob29sIjp7InNjaG9vbElkIjoxLCJzY2hvb2xOYW1lIjoiV2FzZWRhIFVuaXZlcnNpdHkiLCJyZWdleCI6IlxcYlteXFxzXStAd2FzZWRhXFwuanBcXGIifSwidXNlck5pY2tuYW1lIjoi7ZuIIiwidXNlckVtYWlsIjoieG9ydWRmbDc3MkBuYXZlci5jb20iLCJ1c2VySWQiOiJhYmNkIiwidXNlckluZm9JZCI6MjE4LCJub3RpZmljYXRpb25Ub2tlbiI6InJpZ2h0Q2FzZSJ9.E0CSycn5hUDS8HFg6dFHn-KQl3CDd7EoDU2gO1CqpsudtYG7daO7X8XliNPn0TNXceMPW2wG-oqbvk3wgxOEpQ")
            }
            Log.d("test123","$response")

            withContext(Dispatchers.Main) {
                if (response.code() == 200) { // 서버 통신 success
                    val result = response.body()
                    val getItems = result!!.getAsJsonArray("jobs")

                    for (getItem in getItems) {
                        val jsonObject = getItem as JsonObject
                        //Log.d("getJobsInfoFavorite", "${jsonObject}")
                        _jobsMyFavoriteArrayList.value!!.add(
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
                    _isJobMyFavoriteApiResultFail.postValue(false)
                } else { // 서버 통신 fail
                    _isJobMyFavoriteApiResultFail.postValue(true)
                }
            }
            ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
        }
    }


    fun setSearchWord(string: String){
        _searchWord.value = string
    }



}