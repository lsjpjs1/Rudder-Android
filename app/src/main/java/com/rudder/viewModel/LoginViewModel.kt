package com.rudder.viewModel



import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.LoginInfo
import com.rudder.data.dto.LoginRequestInfo
import com.rudder.data.local.App
import com.rudder.data.local.App.Companion.prefs
import com.rudder.data.remote.NoticeRequest
import com.rudder.data.repository.Repository
import com.rudder.data.repository.RepositoryLogin
import com.rudder.data.repository.RepositoryNotice
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginViewModel() : ViewModel() {

    private val NOTIFICATION_TOKEN_KEY = "notificationKey"
    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    private val _showLoginErrorToast = MutableLiveData<Event<Boolean>>()
    private val _startMainActivity = MutableLiveData<Event<Boolean>>()
    private val _startSignUpActivity = MutableLiveData<Event<Boolean>>()
    private val _startForgotActivity = MutableLiveData<Event<Boolean>>()
    private val _noticeResponse = MutableLiveData<String>()
    var noticeAlreadyShow = false
    val noticeResponse:LiveData<String> = _noticeResponse
    val _autoLogin = MutableLiveData<Event<Boolean>>()
    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val showLoginErrorToast: LiveData<Event<Boolean>> = _showLoginErrorToast
    val startMainActivity: LiveData<Event<Boolean>> = _startMainActivity
    val startSignUpActivity: LiveData<Event<Boolean>> = _startSignUpActivity
    val startForgotActivity : LiveData<Event<Boolean>> = _startForgotActivity
    val autoLogin : LiveData<Event<Boolean>> = _autoLogin

    val _loginResultFlag = MutableLiveData<Int>()
    val loginResultFlag: LiveData<Int> = _loginResultFlag

    private val repository = Repository()
    private val repositoryLogin = RepositoryLogin()
    private val repositoryNotice = RepositoryNotice()

    private val key = BuildConfig.TOKEN_KEY



    init {
        _userId.value = ""
        _userPassword.value = ""
        _showLoginErrorToast.value = Event(false)
    }


    fun getNoticeFun() { // Spring
        val version = BuildConfig.VERSION_NAME
        CoroutineScope(Dispatchers.Main).launch {
            val result = repositoryNotice.noticeApiCall(NoticeRequest("android", version))
            Log.d("test123","${result.body()}")
            when (result.code()) {
                200 -> { // 성공 코드
                    _noticeResponse.value = result.body()!!.noticeBody
                }
                else -> { // 그 외 나머지 서버 에러 코드
                    _noticeResponse.value = "Error Exist"
                }
            }
        }
    }


//    fun getNotice(){
//        try{
//            GlobalScope.async {
//                val version = BuildConfig.VERSION_NAME
//                val response = Repository().getNotice(NoticeRequest("android",version))
//                viewModelScope.launch{
//                    _noticeResponse.value = response
//                }
//            }
//        } catch (e: Exception){
//            _noticeResponse.value = NoticeResponse(true,"Error Exist")
//        }
//    }

    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        if (check) {
            _autoLogin.value = Event(true)
            prefs.setValue("autoLogin","true")
        } else {
            _autoLogin.value = Event(false)
            prefs.setValue("autoLogin","false")
        }
    }


    fun callSignUp(){
        _startSignUpActivity.value = Event(true)
    }


    fun checkToken(){
        CoroutineScope(Dispatchers.Main).launch {

            Log.d("checkTokenApiCall_asd", "${App.prefs.getValue(key)}")
            val resultCode = repositoryLogin.checkTokenApiCall(App.prefs.getValue(key)!!)
            if (resultCode == 204) { // token check 성공
                _startMainActivity.value = Event(true)
            } else { // token check 실패
                _showLoginErrorToast.value = Event(true)
            }
        }
    }




    fun callLogin(){
        try{
            GlobalScope.launch {
                ProgressBarUtil._progressBarFlag.postValue(Event(true))
                val result = repository.login(LoginInfo(_userId.value!!,_userPassword.value!!,App.prefs.getValue(NOTIFICATION_TOKEN_KEY)!!,"android"))
                viewModelScope.launch{
                    if(result){
                        _startMainActivity.value = Event(true)
                    }else{
                        _showLoginErrorToast.value = Event(true)
                    }
                }
            }
        }catch (e: Exception){
            _showLoginErrorToast.value = Event(true)
        }

    }


    fun clickLogin() { // Sign Up, Complete!
        CoroutineScope(Dispatchers.Main).launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))
            val resultCode = repositoryLogin.loginApiCall(LoginRequestInfo(App.prefs.getValue(NOTIFICATION_TOKEN_KEY)!!,"android", _userId.value!!,_userPassword.value!!))
            Log.d("resultCode", "$resultCode")

            when (resultCode) {
                200 -> { // 성공 코드
                    _loginResultFlag.postValue(201)
                }
                404 -> { // 존재 하지 않는 이메일 아이디
                    _loginResultFlag.postValue(404)
                }
                401 -> { // 인증되지 않은 이메일
                    _loginResultFlag.postValue(401)
                }
                402 -> { // 비밀번호 틀림
                    _loginResultFlag.postValue(402)
                }
                406 -> { // userId 혹은 userPassword null 인 경우
                    _loginResultFlag.postValue(406)
                }
                else -> { // 그 외 나머지 서버 에러 코드
                    _loginResultFlag.postValue(-1)
                }
            }
        }
    }



    fun callForgot(){
        _startForgotActivity.value = Event(true)
    }


    fun clearIdAndPassword(){
        _userId.value = ""
        _userPassword.value = ""
    }

}